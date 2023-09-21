package com.arsiu.eduhub.repository.impl

import com.arsiu.eduhub.model.Assignment
import com.arsiu.eduhub.model.Lesson
import com.arsiu.eduhub.repository.AssignmentRepository
import com.arsiu.eduhub.repository.LessonRepository
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.findAll
import org.springframework.data.mongodb.core.findById
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
class LessonRepositoryImpl(
    private val reactiveMongoTemplate: ReactiveMongoTemplate,
    private val assignmentRepository: AssignmentRepository
) : LessonRepository {

    override fun createCascade(entity: Lesson): Mono<Lesson> {
        resetField(entity, "id")
        return reactiveMongoTemplate.save(entity)
            .flatMap { lesson ->
                Flux.fromIterable(lesson.assignments)
                    .doOnNext { it.lessonId = lesson.id }
                    .flatMap { assignmentRepository.createCascade(it) }
                    .then(Mono.just(lesson))
            }
            .flatMap { lesson -> reactiveMongoTemplate.save(lesson) }
    }

    override fun updateCascade(entity: Lesson): Mono<Lesson> =
        findByIdCascade(entity.id)
            .flatMap { existingEntity ->
                if (existingEntity != entity) {

                    val deleteAssignments = Flux.fromIterable(existingEntity.assignments)
                        .filter { existingAssignment ->
                            entity.assignments.none { newAssignment -> existingAssignment.id == newAssignment.id }
                        }.flatMap { assignmentRepository.deleteCascade(it) }

                    val updateAssignments = Flux.fromIterable(entity.assignments)
                        .doOnNext { it.lessonId = entity.id }
                        .flatMap { assignmentRepository.updateCascade(it) }

                    val query = Query.query(Criteria.where("id").`is`(entity.id))

                    deleteAssignments.thenMany(updateAssignments)
                        .then(Mono.defer {
                            val update = Update()
                                .set("id", entity.id)
                                .set("name", entity.name)
                                .set("assignments", entity.assignments)
                            reactiveMongoTemplate.upsert(query, update, Lesson::class.java).thenReturn(entity)
                        })
                } else {
                    Mono.just(existingEntity)
                }
            }
            .switchIfEmpty(Mono.defer { createCascade(entity) })

    override fun findByIdCascade(id: String): Mono<Lesson> =
        reactiveMongoTemplate.findById<Lesson>(id)
            .flatMap { lesson ->
                findAssignmentsForLesson(lesson.id)
                    .collectList()
                    .map { assignments ->
                        lesson.assignments = assignments.toMutableList()
                        lesson
                    }
            }

    override fun findAllCascade(): Flux<Lesson> =
        reactiveMongoTemplate.findAll<Lesson>()
            .flatMap { lesson ->
                assignmentRepository.findAllCascade()
                    .collectList()
                    .map { assignments ->
                        lesson.assignments = assignments.toMutableList()
                        lesson
                    }
            }

    override fun deleteCascade(entity: Lesson): Mono<Void> =
        Flux.fromIterable(entity.assignments)
            .flatMap { assignmentRepository.deleteCascade(it) }
            .then(reactiveMongoTemplate.remove(entity).then())

    private fun findAssignmentsForLesson(lessonId: String): Flux<Assignment> {
        val criteria = Criteria.where("lessonId").`is`(lessonId)
        return reactiveMongoTemplate.find(Query(criteria), Assignment::class.java)
    }

}
