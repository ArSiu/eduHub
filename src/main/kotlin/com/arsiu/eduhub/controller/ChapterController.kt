package com.arsiu.eduhub.controller

import com.arsiu.eduhub.dto.request.ChapterDtoRequest
import com.arsiu.eduhub.dto.response.ChapterDtoResponse
import com.arsiu.eduhub.mapper.ChapterMapper
import com.arsiu.eduhub.service.ChapterService
import com.arsiu.eduhub.service.interfaces.ChapterServiceInterface
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
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
    private val chapterService: ChapterServiceInterface,
    private val chapterMapper: ChapterMapper
) {

    @GetMapping
    fun getAllChapters(): ResponseEntity<List<ChapterDtoResponse>> =
        ResponseEntity(
            chapterMapper.toDtoResponseList(chapterService.findAll()),
            HttpStatus.OK
        )

    @GetMapping("/{id}")
    fun getChapterById(@PathVariable id: Long): ResponseEntity<ChapterDtoResponse> =
        ResponseEntity(
            chapterMapper.toDtoResponse(chapterService.findById(id)),
            HttpStatus.OK
        )

    @PutMapping("/{id}")
    fun updateChapterById(
        @PathVariable id: Long,
        @Valid @RequestBody chapter: ChapterDtoRequest
    ) {
        chapterService.update(
            id,
            chapterMapper.toEntity(chapter)
        )
    }

    @DeleteMapping("/{id}")
    fun deleteChapterById(@PathVariable id: Long) {
        chapterService.delete(id)
    }

}
