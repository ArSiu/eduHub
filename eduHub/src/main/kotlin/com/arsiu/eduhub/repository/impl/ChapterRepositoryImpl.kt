package com.arsiu.eduhub.repository.impl

import com.arsiu.eduhub.model.Chapter
import com.arsiu.eduhub.model.Lesson
import com.arsiu.eduhub.repository.ChapterRepository
import com.arsiu.eduhub.repository.LessonRepository
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.findAll
import org.springframework.data.mongodb.core.findById
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
class ChapterRepositoryImpl(
    private val reactiveMongoTemplate: ReactiveMongoTemplate,
    private val lessonRepository: LessonRepository
) : ChapterRepository {

    override fun createCascade(entity: Chapter): Mono<Chapter> {
        resetField(entity, "id")
        return reactiveMongoTemplate.save(entity)
            .flatMap { chapter ->
                Flux.fromIterable(chapter.lessons)
                    .doOnNext { it.chapterId = chapter.id }
                    .flatMap { lessonRepository.createCascade(it) }
                    .then(Mono.just(chapter))
            }.flatMap { savedChapter -> reactiveMongoTemplate.save(savedChapter) }

    }

    override fun findAllCascade(): Flux<Chapter> =
        reactiveMongoTemplate.findAll<Chapter>()
            .flatMap { chapter ->
                lessonRepository.findAllCascade()
                    .collectList()
                    .map { lessons ->
                        chapter.lessons = lessons.toMutableList()
                        chapter
                    }
            }

    override fun findByIdCascade(id: String): Mono<Chapter> =
        reactiveMongoTemplate.findById<Chapter>(id)
            .flatMap { chapter ->
                findLessonsForChapter(chapter.id)
                    .collectList()
                    .map { lessons ->
                        chapter.lessons = lessons.toMutableList()
                        chapter
                    }
            }

    override fun updateCascade(entity: Chapter): Mono<Chapter> =
        findByIdCascade(entity.id)
            .flatMap { existingEntity ->
                if (existingEntity != entity) {

                    val deleteLessons = Flux.fromIterable(existingEntity.lessons)
                        .filter { f -> entity.lessons.none { s -> f.id == s.id } }
                        .flatMap { lesson -> lessonRepository.deleteCascade(lesson) }

                    val updateLessons = Flux.fromIterable(entity.lessons)
                        .doOnNext { lesson -> lesson.chapterId = entity.id }
                        .flatMap { lesson -> lessonRepository.updateCascade(lesson) }

                    val query = Query.query(Criteria.where("id").`is`(entity.id))

                    deleteLessons.thenMany(updateLessons)
                        .then(Mono.defer {
                            val update = Update()
                                .set("id", entity.id)
                                .set("name", entity.name)
                                .set("lessons", entity.lessons)
                            reactiveMongoTemplate.upsert(query, update, Chapter::class.java).thenReturn(entity)
                        })
                } else {
                    Mono.just(existingEntity)
                }
            }
            .switchIfEmpty(Mono.defer { createCascade(entity) })

    override fun deleteCascade(entity: Chapter): Mono<Void> =
        Flux.fromIterable(entity.lessons)
            .flatMap { lesson -> lessonRepository.deleteCascade(lesson) }
            .then(reactiveMongoTemplate.remove(entity).then())

    private fun findLessonsForChapter(chapterId: String): Flux<Lesson> {
        val criteria = Criteria.where("chapterId").`is`(chapterId)
        return reactiveMongoTemplate.find(Query(criteria), Lesson::class.java)
            .flatMap { lesson ->
                lessonRepository.findByIdCascade(lesson.id)
            }
    }

}
