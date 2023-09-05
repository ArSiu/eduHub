package com.arsiu.eduhub.service

import com.arsiu.eduhub.annotation.NotifyTrigger
import com.arsiu.eduhub.exception.NotFoundException
import com.arsiu.eduhub.model.Chapter
import com.arsiu.eduhub.repository.ChapterRepository
import com.arsiu.eduhub.service.interfaces.ChapterServiceInterface
import com.arsiu.eduhub.service.interfaces.CourseServiceInterface
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service

@Service
class ChapterService @Autowired constructor(
    private val chapterRepository: ChapterRepository,
    @Lazy val courseService: CourseServiceInterface
) : ChapterServiceInterface {

    override fun findAll(): List<Chapter> = chapterRepository.findAll().toList()

    override fun findById(id: String): Chapter =
        chapterRepository.findById(id).orElseThrow { NotFoundException("Chapter with ID $id not found") }

    override fun create(entity: Chapter): Chapter {
        courseService.findById(entity.course.id)
        return chapterRepository.createCascade(entity)
    }

    @NotifyTrigger("Chapter was updated ")
    override fun update(id: String, entity: Chapter): Chapter {
        entity.id = findById(id).id
        delete(id)
        return create(entity)
    }

    override fun delete(id: String) = chapterRepository.deleteCascade(findById(id))

}
