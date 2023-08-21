package com.arsiu.eduhub.service

import com.arsiu.eduhub.exception.NotFoundException
import com.arsiu.eduhub.model.Lesson
import com.arsiu.eduhub.repository.LessonRepository
import com.arsiu.eduhub.service.interfaces.LessonServiceInterface
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service

@Service
class LessonService @Autowired constructor(
    val lessonRepository: LessonRepository,
    @Lazy val chapterService: ChapterService,
    val assignmentService: AssignmentService
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

    override fun update(id: Long, entity: Lesson) {
        entity.id = findById(id).id
        create(entity)
    }

    override fun delete(id: Long) = lessonRepository.deleteById(id)

}
