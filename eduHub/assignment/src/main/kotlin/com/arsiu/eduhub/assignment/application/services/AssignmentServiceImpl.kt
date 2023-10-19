package com.arsiu.eduhub.assignment.application.services

import com.arsiu.eduhub.assignment.application.port.AssignmentPersistenceRepository
import com.arsiu.eduhub.assignment.application.port.AssignmentService
import com.arsiu.eduhub.assignment.domain.Assignment
import com.arsiu.eduhub.common.infrastructure.annotation.NotifyTrigger
import com.arsiu.eduhub.common.application.exception.NotFoundException
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class AssignmentServiceImpl(
    private val assignmentRepository: AssignmentPersistenceRepository
) : AssignmentService {

    override fun create(entity: Assignment): Mono<Assignment> {
        resetField(entity, "id")
        return assignmentRepository.save(entity)
    }

    override fun findAll(): Flux<Assignment> =
        assignmentRepository.findAll()

    override fun findById(id: String): Mono<Assignment> =
        assignmentRepository.findById(id)
            .switchIfEmpty(Mono.error(NotFoundException("Assigment with ID $id not found")))

    @NotifyTrigger("Assignment was updated ")
    override fun update(entity: Assignment): Mono<Assignment> =
        assignmentRepository.findById(entity.id)
            .flatMap { existingEntity ->
                if (existingEntity != entity) {
                    assignmentRepository.upsert(entity)
                } else {
                    Mono.just(existingEntity)
                }
            }
            .switchIfEmpty(Mono.defer { create(entity) })

    override fun delete(id: String): Mono<Void> =
        findById(id).flatMap { assignmentRepository.remove(it) }

    override fun createAssignmentsForLesson(
        lessonId: String,
        assignments: MutableList<Assignment>
    ): Mono<Void> {
        return Flux.fromIterable(assignments)
            .doOnNext { it.lessonId = lessonId }
            .flatMap { assignment -> saveAndUpdateId(assignment) }
            .then()
    }

    private fun saveAndUpdateId(assignment: Assignment): Mono<Assignment> {
        return create(assignment)
            .doOnNext { createdAssignment -> assignment.id = createdAssignment.id }
    }

    override fun deleteRemovedAssignmentsForLesson(
        existingAssignments: List<Assignment>,
        newAssignments: List<Assignment>
    ): Mono<Void> =
        Flux.fromIterable(existingAssignments)
            .filter { existingAssignment ->
                newAssignments.none {
                    it.id == existingAssignment.id
                }
            }
            .flatMap { delete(it.id) }
            .then()

    override fun updateAssignmentsForLesson(
        lessonId: String,
        assignments: List<Assignment>
    ): Flux<Assignment> =
        Flux.fromIterable(assignments)
            .doOnNext { it.lessonId = lessonId }
            .flatMap {
                update(it)
                    .doOnNext { updatedAssignment -> it.id = updatedAssignment.id }
            }

    override fun findAssignmentsForLesson(lessonId: String): Flux<Assignment> {
        val criteria = Criteria.where("lessonId").`is`(lessonId)
        return assignmentRepository.find(Query(criteria))
    }

}
