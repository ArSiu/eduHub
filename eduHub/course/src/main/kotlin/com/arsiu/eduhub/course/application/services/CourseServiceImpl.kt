package com.arsiu.eduhub.course.application.services

import com.arsiu.eduhub.chapter.application.port.ChapterService
import com.arsiu.eduhub.common.application.exception.NotFoundException
import com.arsiu.eduhub.common.infrastructure.annotation.NotifyTrigger
import com.arsiu.eduhub.course.application.port.CourseCachingRepository
import com.arsiu.eduhub.course.application.port.CoursePersistenceRepository
import com.arsiu.eduhub.course.application.port.CourseService
import com.arsiu.eduhub.course.domain.Course
import org.springframework.data.domain.Sort.Direction.DESC
import org.springframework.data.mongodb.core.aggregation.Aggregation
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class CourseServiceImpl(
    private val coursePersistenceRepository: CoursePersistenceRepository,
    private val chapterService: ChapterService,
    private val courseCachingRepository: CourseCachingRepository
) : CourseService {

    @NotifyTrigger("New Course Available ")
    override fun create(entity: Course): Mono<Course> {
        resetField(entity, "id")
        return coursePersistenceRepository.save(entity)
            .flatMap { course ->
                chapterService.createChaptersForCourse(course.id, course.chapters)
                    .then(Mono.just(course))
            }
            .flatMap(coursePersistenceRepository::upsert)
            .doOnSuccess { createdEntity ->
                courseCachingRepository.save(createdEntity).subscribe()
            }
    }

    override fun findAll(): Flux<Course> =
        courseCachingRepository.findAll()
            .switchIfEmpty(fallbackToMongo())

    private fun fallbackToMongo(): Flux<Course> =
        coursePersistenceRepository.findAll()
            .flatMap(this::enrichWithChapters)
            .doOnNext(this::saveToRedis)

    private fun enrichWithChapters(course: Course): Mono<Course> =
        chapterService.findChaptersForCourse(course.id)
            .collectList()
            .map { chapters ->
                course.chapters = chapters.toMutableList()
                course
            }

    private fun saveToRedis(course: Course) =
        courseCachingRepository.save(course).subscribe()

    override fun findById(id: String): Mono<Course> =
        courseCachingRepository.findById(id)
            .switchIfEmpty(
                coursePersistenceRepository.findById(id)
                    .flatMap(this::enrichWithChaptersAndSaveToRedis)
            )
            .switchIfEmpty(Mono.error(NotFoundException("Course with ID $id not found")))

    private fun enrichWithChaptersAndSaveToRedis(course: Course): Mono<Course> =
        chapterService.findChaptersForCourse(course.id)
            .collectList()
            .doOnNext { chapters -> course.chapters = chapters.toMutableList() }
            .doOnNext { saveToRedis(course) }
            .thenReturn(course)

    override fun update(entity: Course): Mono<Course> =
        findById(entity.id)
            .flatMap { existingEntity ->
                if (existingEntity != entity) {
                    chapterService.deleteNonExistentChaptersForCourse(
                        existingEntity.chapters,
                        entity.chapters
                    )
                        .thenMany(
                            chapterService.updateChaptersForCourse(
                                entity.id,
                                entity.chapters
                            )
                        )
                        .collectList()
                        .flatMap { updatedLessons ->
                            entity.chapters = updatedLessons
                            coursePersistenceRepository.upsert(entity)
                        }
                        .doOnSuccess(this::saveToRedis)
                } else {
                    Mono.just(existingEntity)
                }
            }

    override fun delete(id: String): Mono<Void> =
        findById(id).flatMap {
            Flux.fromIterable(it.chapters)
                .flatMap { chapterService.delete(it.id) }
                .then(coursePersistenceRepository.remove(it))
                .then(courseCachingRepository.deleteById(id))
        }

    override fun sortCoursesByInners(): Flux<Course> {
        val aggregation: Aggregation = Aggregation.newAggregation(
            Aggregation.match(Criteria.where("_id").exists(true)),
            Aggregation.unwind("chapters"),
            Aggregation.group("_id")
                .count().`as`("chapterCount")
                .first("name").`as`("name")
                .first("ownerId").`as`("ownerId"),
            Aggregation.sort(DESC, "chapterCount"),
            Aggregation.project("_id")
        )

        return coursePersistenceRepository.aggregate(aggregation)
            .flatMap { courseAggregated ->
                findById(courseAggregated.id)
            }
    }

}
