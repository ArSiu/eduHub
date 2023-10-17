package com.arsiu.eduhub.lesson.application.services

import com.arsiu.eduhub.assignment.application.port.AssignmentService
import com.arsiu.eduhub.common.application.exception.NotFoundException
import com.arsiu.eduhub.common.infrastructure.annotation.NotifyTrigger
import com.arsiu.eduhub.lesson.application.port.LessonPersistenceRepository
import com.arsiu.eduhub.lesson.application.port.LessonService
import com.arsiu.eduhub.lesson.domain.Lesson
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service("lessonService")
class LessonServiceImpl(
    private val lessonRepository: LessonPersistenceRepository,
    private val assignmentService: AssignmentService,
) : LessonService {

    override fun create(entity: Lesson): Mono<Lesson> {
        resetField(entity, "id")
        return lessonRepository.save(entity)
            .flatMap { lesson ->
                assignmentService.createAssignmentsForLesson(lesson.id, lesson.assignments)
                    .then(Mono.just(lesson))
            }
            .flatMap(lessonRepository::upsert)
    }

    override fun createLessonsForChapter(
        chapterId: String,
        lessons: MutableList<Lesson>
    ): Mono<Void> {
        return Flux.fromIterable(lessons)
            .doOnNext { it.chapterId = chapterId }
            .flatMap { lesson ->
                create(lesson)
                    .doOnNext { createdLesson -> lesson.id = createdLesson.id }
            }
            .then()
    }

    override fun findAll(): Flux<Lesson> =
        lessonRepository.findAll()
            .flatMap(this::enrichWithAssignments)

    override fun findById(id: String): Mono<Lesson> =
        lessonRepository.findById(id)
            .flatMap(this::enrichWithAssignments)
            .switchIfEmpty(Mono.error(NotFoundException("Lesson with ID $id not found")))

    private fun enrichWithAssignments(lesson: Lesson): Mono<Lesson> =
        assignmentService.findAssignmentsForLesson(lesson.id)
            .collectList()
            .doOnNext { assignments -> lesson.assignments = assignments.toMutableList() }
            .thenReturn(lesson)

    @NotifyTrigger("Lesson was updated ")
    override fun update(entity: Lesson): Mono<Lesson> =
        findById(entity.id)
            .flatMap { existingEntity ->
                if (existingEntity != entity) {
                    assignmentService.deleteRemovedAssignmentsForLesson(
                        existingEntity.assignments,
                        entity.assignments
                    )
                        .thenMany(
                            assignmentService.updateAssignmentsForLesson(
                                entity.id,
                                entity.assignments
                            )
                        )
                        .collectList()
                        .flatMap { updatedAssignments ->
                            entity.assignments = updatedAssignments
                            lessonRepository.upsert(entity)
                        }
                } else {
                    Mono.just(existingEntity)
                }
            }
            .onErrorResume(NotFoundException::class.java) { create(entity) }

    override fun delete(id: String): Mono<Void> =
        findById(id).flatMap {
            Flux.fromIterable(it.assignments)
                .flatMap { assignmentService.delete(it.id) }
                .then(lessonRepository.remove(it))
        }

    override fun findLessonsForChapter(chapterId: String): Flux<Lesson> {
        val criteria = Criteria.where("chapterId").`is`(chapterId)
        return lessonRepository.find(Query(criteria))
            .flatMap { lesson ->
                findById(lesson.id)
            }
    }

    override fun deleteRemovedLessonsForChapter(
        existingLessons: List<Lesson>,
        newLessons: List<Lesson>
    ): Mono<Void> =
        Flux.fromIterable(existingLessons)
            .filter { existingLesson ->
                newLessons.none { it.id == existingLesson.id }
            }
            .flatMap { delete(it.id) }
            .then()

    override fun updateLessonsForChapter(chapterId: String, lessons: List<Lesson>): Flux<Lesson> =
        Flux.fromIterable(lessons)
            .doOnNext { it.chapterId = chapterId }
            .flatMap { update(it).doOnNext { updatedLesson -> it.id = updatedLesson.id } }
}
