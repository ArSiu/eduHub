package com.arsiu.eduhub.repository.impl

import com.arsiu.eduhub.model.Chapter
import com.arsiu.eduhub.model.Course
import com.arsiu.eduhub.repository.ChapterRepository
import com.arsiu.eduhub.repository.CourseRepository
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.aggregation.Aggregation
import org.springframework.data.mongodb.core.aggregation.Aggregation.group
import org.springframework.data.mongodb.core.aggregation.Aggregation.match
import org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation
import org.springframework.data.mongodb.core.aggregation.Aggregation.project
import org.springframework.data.mongodb.core.aggregation.Aggregation.sort
import org.springframework.data.mongodb.core.aggregation.Aggregation.unwind
import org.springframework.data.mongodb.core.findAll
import org.springframework.data.mongodb.core.findById
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
class CourseRepositoryImpl(
    private val reactiveMongoTemplate: ReactiveMongoTemplate,
    private val chapterRepository: ChapterRepository
) : CourseRepository {

    override fun createCascade(entity: Course): Mono<Course> {
        resetField(entity, "id")
        return reactiveMongoTemplate.save(entity)
            .flatMap { course ->
                Flux.fromIterable(entity.chapters)
                    .doOnNext { it.courseId = course.id }
                    .flatMap { chapterRepository.createCascade(it) }
                    .then(Mono.just(course))
            }
            .flatMap { course -> reactiveMongoTemplate.save(course) }
    }

    override fun findAllCascade(): Flux<Course> =
        reactiveMongoTemplate.findAll<Course>()
            .flatMap { course ->
                chapterRepository.findAllCascade()
                    .collectList()
                    .map { chapters ->
                        course.chapters = chapters.toMutableList()
                        course
                    }
            }

    override fun findByIdCascade(id: String): Mono<Course> =
        reactiveMongoTemplate.findById<Course>(id)
            .flatMap { course ->
                populateChaptersForCourse(course)
                    .collectList()
                    .map { chapters ->
                        course.chapters = chapters.toMutableList()
                        course
                    }
            }

    override fun updateCascade(entity: Course): Mono<Course> =
        findByIdCascade(entity.id)
            .flatMap { existingEntity ->
                if (existingEntity != entity) {

                    val deleteChapters = Flux.fromIterable(existingEntity.chapters)
                        .filter { existingChapter ->
                            entity.chapters.none { newChapter -> existingChapter.id == newChapter.id }
                        }.flatMap { chapterRepository.deleteCascade(it) }

                    val updateChapters = Flux.fromIterable(entity.chapters)
                        .doOnNext { it.courseId = entity.id }
                        .flatMap { chapterRepository.updateCascade(it) }

                    val query = Query.query(Criteria.where("id").`is`(entity.id))

                    deleteChapters.thenMany(updateChapters)
                        .then(Mono.defer {
                            val update = Update()
                                .set("id", entity.id)
                                .set("name", entity.name)
                                .set("ownerId", entity.ownerId)
                                .set("chapters", entity.chapters)
                            reactiveMongoTemplate.upsert(query, update, Course::class.java).thenReturn(entity)
                        })
                } else {
                    Mono.just(existingEntity)
                }
            }

    override fun deleteCascade(entity: Course): Mono<Void> =
        Flux.fromIterable(entity.chapters)
            .flatMap { chapterRepository.deleteCascade(it) }
            .then(reactiveMongoTemplate.remove(entity).then())

    override fun sortCoursesByInners(): Flux<Course> {
        val aggregation: Aggregation = newAggregation(
            match(Criteria.where("_id").exists(true)),
            unwind("chapters"),
            group("_id")
                .count().`as`("chapterCount")
                .first("name").`as`("name")
                .first("ownerId").`as`("ownerId"),
            sort(org.springframework.data.domain.Sort.Direction.DESC, "chapterCount"),
            project("_id")
        )

        return reactiveMongoTemplate.aggregate(aggregation, "course", Course::class.java)
            .flatMap { courseAggregated ->
                findByIdCascade(courseAggregated.id)
            }
    }

    private fun populateChaptersForCourse(course: Course): Flux<Chapter> {
        val criteria = Criteria.where("courseId").`is`(course.id)
        return reactiveMongoTemplate.find(Query(criteria), Chapter::class.java)
            .flatMap { chapter ->
                chapterRepository.findByIdCascade(chapter.id)
            }
    }

}
