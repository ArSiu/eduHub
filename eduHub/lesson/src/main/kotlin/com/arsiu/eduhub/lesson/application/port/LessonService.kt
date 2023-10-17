package com.arsiu.eduhub.lesson.application.port

import com.arsiu.eduhub.common.application.port.GeneralService
import com.arsiu.eduhub.lesson.domain.Lesson
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface LessonService : GeneralService<Lesson, String> {

    fun findLessonsForChapter(chapterId: String): Flux<Lesson>

    fun createLessonsForChapter(chapterId: String, lessons: MutableList<Lesson>): Mono<Void>

    fun deleteRemovedLessonsForChapter(existingLessons: List<Lesson>, newLessons: List<Lesson>): Mono<Void>

    fun updateLessonsForChapter(chapterId: String, lessons: List<Lesson>): Flux<Lesson>

}
