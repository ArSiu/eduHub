package com.arsiu.eduhub.service

import com.arsiu.eduhub.annotation.NotifyTrigger
import com.arsiu.eduhub.exception.NotFoundException
import com.arsiu.eduhub.model.Lesson
import com.arsiu.eduhub.repository.LessonRepository
import com.arsiu.eduhub.service.interfaces.AssignmentServiceInterface
import com.arsiu.eduhub.service.interfaces.ChapterServiceInterface
import com.arsiu.eduhub.service.interfaces.LessonServiceInterface
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service

@Service
class LessonService @Autowired constructor(
    private val lessonRepository: LessonRepository,
    private val assignmentService: AssignmentServiceInterface,
    @Lazy val chapterService: ChapterServiceInterface
) : LessonServiceInterface {
    override fun findAll(): List<Lesson> = lessonRepository.findAll().toList()

    override fun findById(id: Long): Lesson =
        lessonRepository.findById(id).orElseThrow { NotFoundException("Lesson with ID $id not found") }

    override fun create(entity: Lesson): Lesson {
        val chapter = chapterService.findById(entity.chapter.id)
        entity.chapter = chapter

        val assignments = entity.assignments.toList()
        entity.assignments.clear()

        val createdLesson = lessonRepository.save(entity)

        val updatedAssignments = assignments.map { assignment ->
            assignment.lesson = createdLesson
            assignmentService.create(assignment)
        }

        createdLesson.assignments.addAll(updatedAssignments)

        return lessonRepository.save(createdLesson)
    }

    @NotifyTrigger("Lesson was updated ")
    override fun update(id: Long, entity: Lesson): Lesson {
        entity.id = findById(id).id
        return create(entity)
    }

    override fun delete(id: Long) = lessonRepository.deleteById(id)

}
