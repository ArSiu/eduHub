package com.arsiu.eduhub.lesson.application.services

import com.arsiu.eduhub.assignment.application.port.AssignmentService
import com.arsiu.eduhub.assignment.domain.Assignment
import com.arsiu.eduhub.common.application.annotation.NotifyTrigger
import com.arsiu.eduhub.common.application.exception.NotFoundException
import com.arsiu.eduhub.lesson.application.port.LessonRepository
import com.arsiu.eduhub.lesson.application.port.LessonService
import com.arsiu.eduhub.lesson.domain.Lesson
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service("lessonService")
class LessonServiceImpl(
    private val lessonRepository: LessonRepository,
    private val assignmentService: AssignmentService,
) : LessonService {

    override fun create(entity: Lesson): Mono<Lesson> {
        resetField(entity, "id")
        return lessonRepository.save(entity)
            .flatMap { lesson -> updateAssignmentIdsAndSave(lesson) }
            .flatMap(lessonRepository::upsert)
    }

    private fun updateAssignmentIdsAndSave(lesson: Lesson): Mono<Lesson> {
        return Flux.fromIterable(lesson.assignments)
            .doOnNext { it.lessonId = lesson.id }
            .flatMap { assignment -> saveAndUpdateId(assignment) }
            .then(Mono.just(lesson))
    }

    private fun saveAndUpdateId(assignment: Assignment): Mono<Assignment> {
        return assignmentService.create(assignment)
            .doOnNext { createdAssignment -> assignment.id = createdAssignment.id }
    }

    override fun findAll(): Flux<Lesson> =
        lessonRepository.findAll()
            .flatMap { lesson ->
                assignmentService.findAssignmentsForLesson(lesson.id)
                    .collectList()
                    .map { assignments ->
                        lesson.assignments = assignments.toMutableList()
                        lesson
                    }
            }

    override fun findById(id: String): Mono<Lesson> =
        lessonRepository.findById(id)
            .flatMap { lesson ->
                assignmentService.findAssignmentsForLesson(lesson.id)
                    .collectList()
                    .map { assignments ->
                        lesson.assignments = assignments.toMutableList()
                        lesson
                    }
            }
            .switchIfEmpty(Mono.error(NotFoundException("Lesson with ID $id not found")))


    @NotifyTrigger("Lesson was updated ")
    override fun update(entity: Lesson): Mono<Lesson> =
        findById(entity.id)
            .flatMap { existingEntity ->
                if (existingEntity != entity) {

                    val deleteAssignments = Flux.fromIterable(existingEntity.assignments)
                        .filter { existingAssignment ->
                            entity.assignments.none { newAssignment ->
                                existingAssignment.id == newAssignment.id
                            }
                        }.flatMap { assignmentService.delete(it.id) }

                    val updateAssignments = Flux.fromIterable(entity.assignments)
                        .doOnNext { it.lessonId = entity.id }
                        .flatMap {
                            assignmentService.update(it)
                                .doOnNext { updatedAssignment ->
                                    it.id = updatedAssignment.id
                                }
                        }

                    deleteAssignments.thenMany(updateAssignments)
                        .then(Mono.defer {
                            lessonRepository.upsert(entity)
                        })
                } else {
                    Mono.just(existingEntity)
                }
            }.onErrorResume(NotFoundException::class.java) { Mono.defer { create(entity) } }


    override fun delete(id: String): Mono<Void> =
        findById(id).flatMap {
            Flux.fromIterable(it.assignments)
                .flatMap { assignmentService.delete(it.id) }
                .then(lessonRepository.remove(it))
        }

    override fun findLessonsForChapter(chapterId: String): Flux<Lesson> {
        val criteria = Criteria.where("chapterId").`is`(chapterId)
        return lessonRepository.find(Query(criteria))
            .flatMap { lesson ->
                findById(lesson.id)
            }
    }
}
