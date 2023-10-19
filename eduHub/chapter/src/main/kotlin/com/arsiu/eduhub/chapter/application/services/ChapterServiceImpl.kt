package com.arsiu.eduhub.chapter.application.services

import com.arsiu.eduhub.chapter.application.port.ChapterPersistenceRepository
import com.arsiu.eduhub.chapter.application.port.ChapterService
import com.arsiu.eduhub.chapter.domain.Chapter
import com.arsiu.eduhub.common.application.exception.NotFoundException
import com.arsiu.eduhub.common.infrastructure.annotation.NotifyTrigger
import com.arsiu.eduhub.lesson.application.port.LessonService
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class ChapterServiceImpl(
    private val chapterRepository: ChapterPersistenceRepository,
    private val lessonService: LessonService,
) : ChapterService {

    override fun create(entity: Chapter): Mono<Chapter> {
        resetField(entity, "id")
        return chapterRepository.save(entity)
            .flatMap { chapter ->
                lessonService.createLessonsForChapter(chapter.id, chapter.lessons)
                    .then(Mono.just(chapter))
            }
            .flatMap(chapterRepository::upsert)
    }

    override fun findAll(): Flux<Chapter> =
        chapterRepository.findAll()
            .flatMap(this::enrichWithLessons)

    override fun findById(id: String): Mono<Chapter> =
        chapterRepository.findById(id)
            .flatMap(this::enrichWithLessons)
            .switchIfEmpty(Mono.error(NotFoundException("Chapter with ID $id not found")))

    @NotifyTrigger("Chapter was updated ")
    override fun update(entity: Chapter): Mono<Chapter> =
        findById(entity.id)
            .flatMap { existingEntity ->
                if (existingEntity != entity) {
                    lessonService.deleteRemovedLessonsForChapter(
                        existingEntity.lessons,
                        entity.lessons
                    )
                        .thenMany(lessonService.updateLessonsForChapter(
                            entity.id,
                            entity.lessons)
                        )
                        .collectList()
                        .flatMap { updatedLessons ->
                            entity.lessons = updatedLessons
                            chapterRepository.upsert(entity)
                        }
                } else {
                    Mono.just(existingEntity)
                }
            }
            .onErrorResume(NotFoundException::class.java) { create(entity) }

    override fun delete(id: String): Mono<Void> =
        findById(id).flatMap {
            Flux.fromIterable(it.lessons)
                .flatMap { lesson -> lessonService.delete(lesson.id) }
                .then(chapterRepository.remove(it))
        }

    override fun createChaptersForCourse(
        courseId: String,
        chapters: MutableList<Chapter>
    ): Mono<Void> {
        return Flux.fromIterable(chapters)
            .doOnNext { it.courseId = courseId }
            .flatMap { chapter ->
                create(chapter)
                    .doOnNext { createdChapter -> chapter.id = createdChapter.id }
            }
            .then()
    }

    private fun enrichWithLessons(chapter: Chapter): Mono<Chapter> =
        lessonService.findLessonsForChapter(chapter.id)
            .collectList()
            .doOnNext { lessons -> chapter.lessons = lessons.toMutableList() }
            .thenReturn(chapter)

    override fun findChaptersForCourse(courseId: String): Flux<Chapter> {
        val criteria = Criteria.where("courseId").`is`(courseId)
        return chapterRepository.find(Query(criteria))
            .flatMap { chapter ->
                findById(chapter.id)
            }
    }

    override fun deleteNonExistentChaptersForCourse(
        existingChapters: List<Chapter>,
        newChapters: List<Chapter>
    ): Mono<Void> =
        Flux.fromIterable(existingChapters)
            .filter { existingChapter ->
                newChapters.none { newChapter -> existingChapter.id == newChapter.id }
            }
            .flatMap { delete(it.id) }
            .then()

    override fun updateChaptersForCourse(courseId: String, chapters: List<Chapter>): Flux<Chapter> =
        Flux.fromIterable(chapters)
            .doOnNext { it.courseId = courseId }
            .flatMap { update(it).doOnNext { updatedChapter -> it.id = updatedChapter.id } }

}
