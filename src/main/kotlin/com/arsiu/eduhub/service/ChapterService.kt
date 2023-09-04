package com.arsiu.eduhub.service

import com.arsiu.eduhub.annotation.NotifyTrigger
import com.arsiu.eduhub.exception.NotFoundException
import com.arsiu.eduhub.model.Chapter
import com.arsiu.eduhub.repository.ChapterRepository
import com.arsiu.eduhub.service.interfaces.ChapterServiceInterface
import com.arsiu.eduhub.service.interfaces.CourseServiceInterface
import com.arsiu.eduhub.service.interfaces.LessonServiceInterface
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service

@Service
class ChapterService @Autowired constructor(
    private val chapterRepository: ChapterRepository,
    private val lessonService: LessonServiceInterface,
    @Lazy val courseService: CourseServiceInterface
) : ChapterServiceInterface {

    override fun findAll(): List<Chapter> = chapterRepository.findAll().toList()

    override fun findById(id: Long): Chapter =
        chapterRepository.findById(id).orElseThrow { NotFoundException("Chapter with ID $id not found") }

    override fun create(entity: Chapter): Chapter {
        val course = courseService.findById(entity.course.id)
        entity.course = course

        val lessons = entity.lessons.toList()
        entity.lessons.clear()

        val createdChapter = chapterRepository.save(entity)

        val updatedLessons = lessons.map { lesson ->
            lesson.chapter = createdChapter
            lessonService.create(lesson)
        }

        createdChapter.lessons.addAll(updatedLessons)

        return chapterRepository.save(createdChapter)
    }

    @NotifyTrigger("Chapter was updated ")
    override fun update(id: Long, entity: Chapter): Chapter {
        entity.id = findById(id).id
        return create(entity)
    }

    override fun delete(id: Long) = chapterRepository.deleteById(id)

}
