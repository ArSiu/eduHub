package com.arsiu.eduhub.service.impl

import com.arsiu.eduhub.annotation.NotifyTrigger
import com.arsiu.eduhub.exception.NotFoundException
import com.arsiu.eduhub.model.Chapter
import com.arsiu.eduhub.repository.ChapterRepository
import com.arsiu.eduhub.service.ChapterService
import com.arsiu.eduhub.service.CourseService
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service

@Service
class ChapterServiceImpl(
    private val chapterRepository: ChapterRepository,
    @Lazy
    private val courseService: CourseService
) : ChapterService {

    override fun findAll(): List<Chapter> =
        chapterRepository.findAll().toList()

    override fun findById(id: String): Chapter =
        chapterRepository.findById(id).orElseThrow { NotFoundException("Chapter with ID $id not found") }

    override fun create(entity: Chapter): Chapter {
        entity.course.id.let { courseService.findById(it) }
        return chapterRepository.createCascade(entity)
    }

    @NotifyTrigger("Chapter was updated ")
    override fun update(entity: Chapter): Chapter {
        findById(entity.id)
        return chapterRepository.updateCascade(entity)
    }

    override fun delete(id: String) =
        chapterRepository.deleteCascade(findById(id))

}
