package com.arsiu.eduhub.service.impl

import com.arsiu.eduhub.annotation.NotifyTrigger
import com.arsiu.eduhub.exception.NotFoundException
import com.arsiu.eduhub.model.Lesson
import com.arsiu.eduhub.repository.LessonRepository
import com.arsiu.eduhub.service.ChapterService
import com.arsiu.eduhub.service.LessonService
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service

@Service
class LessonServiceImpl(
    private val lessonRepository: LessonRepository,
    @Lazy
    private val chapterService: ChapterService
) : LessonService {

    override fun findAll(): List<Lesson> =
        lessonRepository.findAll().toList()

    override fun findById(id: String): Lesson =
        lessonRepository.findById(id).orElseThrow { NotFoundException("Lesson with ID $id not found") }

    override fun create(entity: Lesson): Lesson {
        entity.chapter.id.let { chapterService.findById(it) }
        return lessonRepository.createCascade(entity)
    }

    @NotifyTrigger("Lesson was updated ")
    override fun update(entity: Lesson): Lesson {
        findById(entity.id)
        return lessonRepository.updateCascade(entity)
    }

    override fun delete(id: String) =
        lessonRepository.deleteCascade(findById(id))

}
