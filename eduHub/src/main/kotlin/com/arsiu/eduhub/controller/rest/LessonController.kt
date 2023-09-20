package com.arsiu.eduhub.controller.rest

import com.arsiu.eduhub.dto.request.LessonDtoRequest
import com.arsiu.eduhub.dto.response.LessonDtoResponse
import com.arsiu.eduhub.mapper.LessonMapper
import com.arsiu.eduhub.service.LessonService
import jakarta.validation.Valid
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
    fun getAllLessons(): List<LessonDtoResponse> =
        lessonMapper.toDtoResponseList(lessonService.findAll())

    @GetMapping("/{id}")
    fun getLessonById(@PathVariable id: String): LessonDtoResponse =
        lessonMapper.toDtoResponse(lessonService.findById(id))

    @PutMapping
    fun updateLessonById(@Valid @RequestBody lesson: LessonDtoRequest): LessonDtoResponse {
        val updated = lessonService.update(lessonMapper.toEntityUpdate(lesson))
        return lessonMapper.toDtoResponse(updated)
    }

    @DeleteMapping("/{id}")
    fun deleteLessonById(@PathVariable id: String) =
        lessonService.delete(id)

}
