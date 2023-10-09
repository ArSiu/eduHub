package com.arsiu.eduhub.chapter.application.ports

import com.arsiu.eduhub.chapter.domain.Chapter
import com.arsiu.eduhub.common.application.ports.service.GeneralService
import reactor.core.publisher.Flux

interface ChapterService : GeneralService<Chapter, String> {
    fun findChaptersForCourse(courseId: String): Flux<Chapter>
}
