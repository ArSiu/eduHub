package com.arsiu.eduhub.course.application.services

import com.arsiu.eduhub.chapter.application.ports.ChapterService
import com.arsiu.eduhub.chapter.domain.Chapter
import com.arsiu.eduhub.common.application.annotation.NotifyTrigger
import com.arsiu.eduhub.common.application.exception.NotFoundException
import com.arsiu.eduhub.course.application.ports.CourseRepository
import com.arsiu.eduhub.course.application.ports.CourseService
import com.arsiu.eduhub.course.domain.Course
import org.springframework.data.domain.Sort.Direction.DESC
import org.springframework.data.mongodb.core.aggregation.Aggregation
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class CourseServiceImpl(
    private val courseRepository: CourseRepository,
    private val chapterService: ChapterService,
) : CourseService {

    @NotifyTrigger("New Course Available ")
    override fun create(entity: Course): Mono<Course> {
        resetField(entity, "id")
        return courseRepository.save(entity)
            .flatMap { course -> updateChapterIdsAndSave(course) }
            .flatMap(courseRepository::upsert)
    }

    private fun updateChapterIdsAndSave(course: Course): Mono<Course> {
        return Flux.fromIterable(course.chapters)
            .doOnNext { it.courseId = course.id }
            .flatMap { chapter -> saveAndUpdateId(chapter) }
            .then(Mono.just(course))
    }

    private fun saveAndUpdateId(chapter: Chapter): Mono<Chapter> {
        return chapterService.create(chapter)
            .doOnNext { createdChapter -> chapter.id = createdChapter.id }
    }

    override fun findAll(): Flux<Course> =
        courseRepository.findAll()
            .flatMap { course ->
                chapterService.findAll()
                    .collectList()
                    .map { chapters ->
                        course.chapters = chapters.toMutableList()
                        course
                    }
            }

    override fun findById(id: String): Mono<Course> =
        courseRepository.findById(id)
            .flatMap { course ->
                chapterService.findChaptersForCourse(course.id)
                    .collectList()
                    .map { chapters ->
                        course.chapters = chapters.toMutableList()
                        course
                    }
            }
            .switchIfEmpty(Mono.error(NotFoundException("Course with ID $id not found")))

    override fun update(entity: Course): Mono<Course> =
        findById(entity.id).flatMap { existingEntity ->
            if (existingEntity != entity) {

                val deleteChapters = Flux.fromIterable(existingEntity.chapters)
                    .filter { existingChapter ->
                        entity.chapters.none { newChapter -> existingChapter.id == newChapter.id }
                    }.flatMap { chapterService.delete(it.id) }

                val updateChapters = Flux.fromIterable(entity.chapters)
                    .doOnNext { it.courseId = entity.id }
                    .flatMap {
                        chapterService.update(it)
                            .doOnNext { updatedChapter ->
                                it.id = updatedChapter.id
                            }
                    }

                deleteChapters.thenMany(updateChapters)
                    .then(Mono.defer {
                        courseRepository.upsert(entity)
                    })
            } else {
                Mono.just(existingEntity)
            }
        }

    override fun delete(id: String): Mono<Void> =
        findById(id).flatMap {
            Flux.fromIterable(it.chapters)
                .flatMap { chapterService.delete(it.id) }
                .then(courseRepository.remove(it))
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

        return courseRepository.aggregate(aggregation)
            .flatMap { courseAggregated ->
                findById(courseAggregated.id)
            }
    }

}
