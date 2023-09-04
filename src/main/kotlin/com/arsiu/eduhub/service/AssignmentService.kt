package com.arsiu.eduhub.service

import com.arsiu.eduhub.annotation.NotifyTrigger
import com.arsiu.eduhub.exception.NotFoundException
import com.arsiu.eduhub.model.Assignment
import com.arsiu.eduhub.repository.AssignmentRepository
import com.arsiu.eduhub.service.interfaces.AssignmentServiceInterface
import com.arsiu.eduhub.service.interfaces.LessonServiceInterface
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service

@Service
class AssignmentService @Autowired constructor(
    private val assignmentRepository: AssignmentRepository,
    @Lazy val lessonService: LessonServiceInterface
) : AssignmentServiceInterface {

    override fun findAll(): List<Assignment> = assignmentRepository.findAll().toList()

    override fun findById(id: Long): Assignment =
        assignmentRepository.findById(id).orElseThrow { NotFoundException("Assigment with ID $id not found") }

    override fun create(entity: Assignment): Assignment {
        lessonService.findById(entity.lesson.id)

        return assignmentRepository.save(entity)
    }

    @NotifyTrigger("Assignment was updated ")
    override fun update(id: Long, entity: Assignment): Assignment {
        entity.id = findById(id).id
        return create(entity)
    }

    override fun delete(id: Long) = assignmentRepository.deleteById(id)

}
