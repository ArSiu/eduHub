package com.arsiu.eduhub.controller

import com.arsiu.eduhub.dto.request.LessonDtoRequest
import com.arsiu.eduhub.dto.response.LessonDtoResponse
import com.arsiu.eduhub.mapper.LessonMapper
import com.arsiu.eduhub.service.LessonService
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
    fun getLessonById(@PathVariable id: Long): ResponseEntity<LessonDtoResponse> =
        ResponseEntity(
            lessonMapper.toDtoResponse(lessonService.findById(id)),
            HttpStatus.OK
        )

    @PutMapping("/{id}")
    fun updateLessonById(
        @PathVariable id: Long,
        @Valid @RequestBody lesson: LessonDtoRequest
    ) {
        lessonService.update(
            id,
            lessonMapper.toEntity(lesson)
        )
    }

    @DeleteMapping("/{id}")
    fun deleteLessonById(@PathVariable id: Long) {
        lessonService.delete(id)
    }

}
