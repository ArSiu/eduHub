package com.arsiu.eduhub.service.impl

import com.arsiu.eduhub.annotation.NotifyTrigger
import com.arsiu.eduhub.exception.NotFoundException
import com.arsiu.eduhub.model.Chapter
import com.arsiu.eduhub.repository.ChapterRepository
import com.arsiu.eduhub.service.ChapterService
import com.arsiu.eduhub.service.CourseService
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class ChapterServiceImpl(
    private val chapterRepository: ChapterRepository,
    @Lazy
    private val courseService: CourseService
) : ChapterService {

    override fun findAll(): Flux<Chapter> =
        chapterRepository.findAllCascade()

    override fun findById(id: String): Mono<Chapter> =
        chapterRepository.findByIdCascade(id)
            .switchIfEmpty(Mono.error(NotFoundException("Chapter with ID $id not found")))

    override fun create(entity: Chapter): Mono<Chapter> =
         courseService.findById(entity.courseId).then(chapterRepository.createCascade(entity))

    @NotifyTrigger("Chapter was updated ")
    override fun update(entity: Chapter): Mono<Chapter> =
        findById(entity.id).then(chapterRepository.updateCascade(entity))

    override fun delete(id: String): Mono<Void> =
        findById(id).flatMap(chapterRepository::deleteCascade)

}
