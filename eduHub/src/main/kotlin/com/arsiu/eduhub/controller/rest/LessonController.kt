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
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/lesson")
class LessonController(
    private val lessonService: LessonService,
    private val lessonMapper: LessonMapper
) {

    @GetMapping
    fun getAllLessons(): Mono<List<LessonDtoResponse>> =
        lessonService.findAll()
            .collectList()
            .map { lessons -> lessonMapper.toDtoResponseList(lessons) }

    @GetMapping("/{id}")
    fun getLessonById(@PathVariable id: String): Mono<LessonDtoResponse> =
        lessonService.findById(id)
            .map { lesson -> lessonMapper.toDtoResponse(lesson) }

    @PutMapping
    fun updateLessonById(@Valid @RequestBody lesson: LessonDtoRequest): Mono<LessonDtoResponse> =
        lessonService.update(lessonMapper.toEntityUpdate(lesson))
            .map { updated -> lessonMapper.toDtoResponse(updated) }

    @DeleteMapping("/{id}")
    fun deleteLessonById(@PathVariable id: String): Mono<Void> =
        lessonService.delete(id)

}
