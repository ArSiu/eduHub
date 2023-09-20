package com.arsiu.eduhub.controller.rest

import com.arsiu.eduhub.dto.request.ChapterDtoRequest
import com.arsiu.eduhub.dto.response.ChapterDtoResponse
import com.arsiu.eduhub.mapper.ChapterMapper
import com.arsiu.eduhub.service.ChapterService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/chapter")
class ChapterController(
    private val chapterService: ChapterService,
    private val chapterMapper: ChapterMapper
) {

    @GetMapping
    fun getAllChapters(): List<ChapterDtoResponse> =
        chapterMapper.toDtoResponseList(chapterService.findAll())

    @GetMapping("/{id}")
    fun getChapterById(@PathVariable id: String): ChapterDtoResponse =
        chapterMapper.toDtoResponse(chapterService.findById(id))

    @PutMapping
    fun updateChapterById(@Valid @RequestBody chapter: ChapterDtoRequest): ChapterDtoResponse {
        val updated = chapterService.update(chapterMapper.toEntityUpdate(chapter))
        return chapterMapper.toDtoResponse(updated)
    }

    @DeleteMapping("/{id}")
    fun deleteChapterById(@PathVariable id: String) =
        chapterService.delete(id)

}
