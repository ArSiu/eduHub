package com.arsiu.eduhub.service

import com.arsiu.eduhub.annotation.NotifyTrigger
import com.arsiu.eduhub.exception.NotFoundException
import com.arsiu.eduhub.model.Lesson
import com.arsiu.eduhub.repository.LessonRepository
import com.arsiu.eduhub.service.interfaces.ChapterServiceInterface
import com.arsiu.eduhub.service.interfaces.LessonServiceInterface
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service

@Service
class LessonService @Autowired constructor(
    private val lessonRepository: LessonRepository,
    @Lazy val chapterService: ChapterServiceInterface
) : LessonServiceInterface {
    override fun findAll(): List<Lesson> = lessonRepository.findAll().toList()

    override fun findById(id: String): Lesson =
        lessonRepository.findById(id).orElseThrow { NotFoundException("Lesson with ID $id not found") }

    override fun create(entity: Lesson): Lesson {
        chapterService.findById(entity.chapter.id)
        return lessonRepository.createCascade(entity)
    }

    @NotifyTrigger("Lesson was updated ")
    override fun update(id: String, entity: Lesson): Lesson {
        entity.id = findById(id).id
        delete(id)
        return create(entity)
    }

    override fun delete(id: String) = lessonRepository.deleteCascade(findById(id))

}
