package com.arsiu.eduhub.service.impl

import com.arsiu.eduhub.annotation.NotifyTrigger
import com.arsiu.eduhub.exception.NotFoundException
import com.arsiu.eduhub.model.Assignment
import com.arsiu.eduhub.repository.AssignmentRepository
import com.arsiu.eduhub.service.AssignmentService
import com.arsiu.eduhub.service.LessonService
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service

@Service
class AssignmentServiceImpl (
    private val assignmentRepository: AssignmentRepository,
    @Lazy
    private val lessonService: LessonService
) : AssignmentService {

    override fun findAll(): List<Assignment> =
        assignmentRepository.findAll().toList()

    override fun findById(id: String): Assignment =
        assignmentRepository.findById(id).orElseThrow { NotFoundException("Assigment with ID $id not found") }

    override fun create(entity: Assignment): Assignment {
        entity.lesson.id.let { lessonService.findById(it) }
        return assignmentRepository.createCascade(entity)
    }

    @NotifyTrigger("Assignment was updated ")
    override fun update(entity: Assignment): Assignment {
        findById(entity.id)
        return assignmentRepository.updateCascade(entity)
    }

    override fun delete(id: String) =
        assignmentRepository.deleteCascade(findById(id))

}
