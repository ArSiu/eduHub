package com.arsiu.eduhub.chapter.application.port

import com.arsiu.eduhub.chapter.domain.Chapter
import com.arsiu.eduhub.common.application.port.GeneralService
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface ChapterService : GeneralService<Chapter, String> {

    fun findChaptersForCourse(courseId: String): Flux<Chapter>

    fun createChaptersForCourse(courseId: String, chapters: MutableList<Chapter>): Mono<Void>

    fun deleteNonExistentChaptersForCourse(existingChapters: List<Chapter>, newChapters: List<Chapter>): Mono<Void>

    fun updateChaptersForCourse(courseId: String, chapters: List<Chapter>): Flux<Chapter>

}
