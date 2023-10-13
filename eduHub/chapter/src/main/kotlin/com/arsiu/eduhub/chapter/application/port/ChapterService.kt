package com.arsiu.eduhub.chapter.application.port

import com.arsiu.eduhub.chapter.domain.Chapter
import com.arsiu.eduhub.common.application.port.service.GeneralService
import reactor.core.publisher.Flux

interface ChapterService : GeneralService<Chapter, String> {
    fun findChaptersForCourse(courseId: String): Flux<Chapter>
}
