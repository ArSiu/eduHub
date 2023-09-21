package com.arsiu.eduhub.service.impl

import com.arsiu.eduhub.annotation.NotifyTrigger
import com.arsiu.eduhub.exception.NotFoundException
import com.arsiu.eduhub.model.Lesson
import com.arsiu.eduhub.repository.LessonRepository
import com.arsiu.eduhub.service.ChapterService
import com.arsiu.eduhub.service.LessonService
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class LessonServiceImpl(
    private val lessonRepository: LessonRepository,
    @Lazy
    private val chapterService: ChapterService
) : LessonService {

    override fun findAll(): Flux<Lesson> =
        lessonRepository.findAllCascade()

    override fun findById(id: String): Mono<Lesson> =
        lessonRepository.findByIdCascade(id)
            .switchIfEmpty(Mono.error(NotFoundException("Lesson with ID $id not found")))

    override fun create(entity: Lesson): Mono<Lesson> =
        chapterService.findById(entity.chapterId).then(lessonRepository.createCascade(entity))

    @NotifyTrigger("Lesson was updated ")
    override fun update(entity: Lesson): Mono<Lesson> =
        findById(entity.id).then(lessonRepository.updateCascade(entity))

    override fun delete(id: String): Mono<Void> =
        findById(id).flatMap { lessonRepository.deleteCascade(it) }
}
