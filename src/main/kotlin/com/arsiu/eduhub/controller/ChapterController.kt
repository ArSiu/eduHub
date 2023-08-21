package com.arsiu.eduhub.controller

import com.arsiu.eduhub.dto.request.ChapterDtoRequest
import com.arsiu.eduhub.dto.response.ChapterDtoResponse
import com.arsiu.eduhub.mapper.ChapterMapper
import com.arsiu.eduhub.service.ChapterService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/chapter")
class ChapterController(
    private val chapterService: ChapterService,
    private val chapterMapper: ChapterMapper
) {

    @GetMapping
    fun getAllChapters(): ResponseEntity<List<ChapterDtoResponse>> =
        ResponseEntity(
            chapterMapper.toDtoResponseList(chapterService.findAll()),
            HttpStatus.OK
        )

    @GetMapping("/{id}")
    fun getChapterById(@PathVariable(value = "id") id: Long): ResponseEntity<ChapterDtoResponse> =
        ResponseEntity(
            chapterMapper.toDtoResponse(chapterService.findById(id)),
            HttpStatus.OK
        )

    @PutMapping("/{id}")
    fun updateChapterById(
        @PathVariable(value = "id") id: Long,
        @Valid @RequestBody chapter: ChapterDtoRequest
    ): ResponseEntity<Void> {
        chapterService.update(
            id,
            chapterMapper.toEntity(chapter)
        )
        return ResponseEntity(HttpStatus.OK)
    }

    @DeleteMapping("/{id}")
    fun deleteChapterById(@PathVariable(value = "id") id: Long): ResponseEntity<Void> {
        chapterService.delete(id)
        return ResponseEntity(HttpStatus.OK)
    }
}
