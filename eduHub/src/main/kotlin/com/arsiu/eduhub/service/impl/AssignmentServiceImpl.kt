package com.arsiu.eduhub.service.impl

import com.arsiu.eduhub.annotation.NotifyTrigger
import com.arsiu.eduhub.exception.NotFoundException
import com.arsiu.eduhub.model.Assignment
import com.arsiu.eduhub.repository.AssignmentRepository
import com.arsiu.eduhub.service.AssignmentService
import com.arsiu.eduhub.service.LessonService
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class AssignmentServiceImpl(
    private val assignmentRepository: AssignmentRepository,
    @Lazy
    private val lessonService: LessonService
) : AssignmentService {

    override fun findAll(): Flux<Assignment> =
        assignmentRepository.findAllCascade()

    override fun findById(id: String): Mono<Assignment> =
        assignmentRepository.findByIdCascade(id)
            .switchIfEmpty(Mono.error(NotFoundException("Assigment with ID $id not found")))

    override fun create(entity: Assignment): Mono<Assignment> =
        lessonService.findById(entity.lessonId).then(assignmentRepository.createCascade(entity))

    @NotifyTrigger("Assignment was updated ")
    override fun update(entity: Assignment): Mono<Assignment> =
        findById(entity.id).then(assignmentRepository.updateCascade(entity))

    override fun delete(id: String): Mono<Void> =
        findById(id).flatMap { assignmentRepository.deleteCascade(it) }

}
