package com.arsiu.eduhub.chapter.infrastructure.rest

import com.arsiu.eduhub.chapter.application.port.ChapterService
import com.arsiu.eduhub.chapter.infrastructure.dto.request.ChapterDtoRequest
import com.arsiu.eduhub.chapter.infrastructure.dto.response.ChapterDtoResponse
import com.arsiu.eduhub.chapter.infrastructure.mapper.ChapterMapper
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/chapter")
class ChapterController(
    private val chapterService: ChapterService,
    private val chapterMapper: ChapterMapper
) {

    @GetMapping
    fun getAllChapters(): Mono<List<ChapterDtoResponse>> =
        chapterService.findAll()
            .collectList()
            .map { chapters -> chapterMapper.toDtoResponseList(chapters) }

    @GetMapping("/{id}")
    fun getChapterById(@PathVariable id: String): Mono<ChapterDtoResponse> =
        chapterService.findById(id)
            .map { chapter -> chapterMapper.toDtoResponse(chapter) }

    @PutMapping
    fun updateChapterById(@Valid @RequestBody chapter: ChapterDtoRequest): Mono<ChapterDtoResponse> =
        chapterService.update(chapterMapper.toEntityUpdate(chapter))
            .map { updated -> chapterMapper.toDtoResponse(updated) }

    @DeleteMapping("/{id}")
    fun deleteChapterById(@PathVariable id: String): Mono<Void> =
        chapterService.delete(id)

}
