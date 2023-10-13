package com.arsiu.eduhub.lesson.application.port

import com.arsiu.eduhub.common.application.port.service.GeneralService
import com.arsiu.eduhub.lesson.domain.Lesson
import reactor.core.publisher.Flux

interface LessonService : GeneralService<Lesson, String> {
    fun findLessonsForChapter(chapterId: String): Flux<Lesson>
}
