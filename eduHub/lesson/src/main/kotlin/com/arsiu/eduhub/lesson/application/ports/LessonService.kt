package com.arsiu.eduhub.lesson.application.ports

import com.arsiu.eduhub.common.application.ports.service.GeneralService
import com.arsiu.eduhub.lesson.domain.Lesson
import reactor.core.publisher.Flux

interface LessonService : GeneralService<Lesson, String> {
    fun findLessonsForChapter(chapterId: String): Flux<Lesson>
}
