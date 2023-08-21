package com.arsiu.eduhub.controller

import com.arsiu.eduhub.dto.request.LessonDtoRequest
import com.arsiu.eduhub.dto.response.LessonDtoResponse
import com.arsiu.eduhub.mapper.LessonMapper
import com.arsiu.eduhub.service.LessonService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/lesson")
class LessonController(
    private val lessonService: LessonService,
    private val lessonMapper: LessonMapper
) {

    @GetMapping
    fun getAllLessons(): ResponseEntity<List<LessonDtoResponse>> =
        ResponseEntity(
            lessonMapper.toDtoResponseList(lessonService.findAll()),
            HttpStatus.OK
        )

    @GetMapping("/{id}")
    fun getLessonById(@PathVariable(value = "id") id: Long): ResponseEntity<LessonDtoResponse> =
        ResponseEntity(
            lessonMapper.toDtoResponse(lessonService.findById(id)),
            HttpStatus.OK
        )

    @PutMapping("/{id}")
    fun updateLessonById(
        @PathVariable(value = "id") id: Long,
        @Valid @RequestBody lesson: LessonDtoRequest
    ): ResponseEntity<Void> {
        lessonService.update(
            id,
            lessonMapper.toEntity(lesson)
        )
        return ResponseEntity(HttpStatus.OK)
    }

    @DeleteMapping("/{id}")
    fun deleteLessonById(@PathVariable(value = "id") id: Long): ResponseEntity<Void> {
        lessonService.delete(id)
        return ResponseEntity(HttpStatus.OK)
    }
}
