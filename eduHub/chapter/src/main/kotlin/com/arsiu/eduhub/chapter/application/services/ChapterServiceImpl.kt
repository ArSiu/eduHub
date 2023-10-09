package com.arsiu.eduhub.chapter.application.services

import com.arsiu.eduhub.chapter.application.ports.ChapterRepository
import com.arsiu.eduhub.chapter.application.ports.ChapterService
import com.arsiu.eduhub.chapter.domain.Chapter
import com.arsiu.eduhub.common.application.annotation.NotifyTrigger
import com.arsiu.eduhub.common.application.exception.NotFoundException
import com.arsiu.eduhub.lesson.application.ports.LessonService
import com.arsiu.eduhub.lesson.domain.Lesson
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class ChapterServiceImpl(
    private val chapterRepository: ChapterRepository,
    private val lessonService: LessonService,
) : ChapterService {

    override fun create(entity: Chapter): Mono<Chapter> {
        resetField(entity, "id")
        return chapterRepository.save(entity)
            .flatMap { chapter -> updateLessonIdsAndSave(chapter) }
            .flatMap(chapterRepository::upsert)
    }

    private fun updateLessonIdsAndSave(chapter: Chapter): Mono<Chapter> {
        return Flux.fromIterable(chapter.lessons)
            .doOnNext { it.chapterId = chapter.id }
            .flatMap { lesson -> saveAndUpdateId(lesson) }
            .then(Mono.just(chapter))
    }

    private fun saveAndUpdateId(lesson: Lesson): Mono<Lesson> {
        return lessonService.create(lesson)
            .doOnNext { createdLesson -> lesson.id = createdLesson.id }
    }

    override fun findAll(): Flux<Chapter> =
        chapterRepository.findAll()
            .flatMap { chapter ->
                lessonService.findAll()
                    .collectList()
                    .map { lessons ->
                        chapter.lessons = lessons.toMutableList()
                        chapter
                    }
            }

    override fun findById(id: String): Mono<Chapter> =
        chapterRepository.findById(id)
            .flatMap { chapter ->
                lessonService.findLessonsForChapter(chapter.id)
                    .collectList()
                    .map { lessons ->
                        chapter.lessons = lessons.toMutableList()
                        chapter
                    }
            }
            .switchIfEmpty(Mono.error(NotFoundException("Chapter with ID $id not found")))

    @NotifyTrigger("Chapter was updated ")
    override fun update(entity: Chapter): Mono<Chapter> =
        findById(entity.id)
            .flatMap { existingEntity ->
                if (existingEntity != entity) {

                    val deleteLessons = Flux.fromIterable(existingEntity.lessons)
                        .filter { f -> entity.lessons.none { s -> f.id == s.id } }
                        .flatMap { lesson -> lessonService.delete(lesson.id) }

                    val updateLessons = Flux.fromIterable(entity.lessons)
                        .doOnNext { lesson -> lesson.chapterId = entity.id }
                        .flatMap {
                            lessonService.update(it)
                                .doOnNext { updatedLesson ->
                                    it.id = updatedLesson.id
                                }
                        }

                    deleteLessons.thenMany(updateLessons)
                        .then(Mono.defer {
                            chapterRepository.upsert(entity)
                        })
                } else {
                    Mono.just(existingEntity)
                }
            }.onErrorResume(NotFoundException::class.java) { Mono.defer { create(entity) } }

    override fun delete(id: String): Mono<Void> =
        findById(id).flatMap {
            Flux.fromIterable(it.lessons)
                .flatMap { lesson -> lessonService.delete(lesson.id) }
                .then(chapterRepository.remove(it))
        }

    override fun findChaptersForCourse(courseId: String): Flux<Chapter> {
        val criteria = Criteria.where("courseId").`is`(courseId)
        return chapterRepository.find(Query(criteria))
            .flatMap { chapter ->
                findById(chapter.id)
            }
    }

}
