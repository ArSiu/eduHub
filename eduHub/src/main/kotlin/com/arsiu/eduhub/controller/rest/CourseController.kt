package com.arsiu.eduhub.controller.rest

import com.arsiu.eduhub.dto.request.CourseDtoRequest
import com.arsiu.eduhub.dto.response.CourseDtoResponse
import com.arsiu.eduhub.mapper.CourseMapper
import com.arsiu.eduhub.service.CourseService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/course")
class CourseController(
    private val courseService: CourseService,
    private val courseMapper: CourseMapper
) {

    @GetMapping
    fun getAllCourses(): Mono<List<CourseDtoResponse>> =
        courseService.findAll()
            .collectList()
            .map { courses -> courseMapper.toDtoResponseList(courses) }

    @GetMapping("/mostElements")
    fun getAllCoursesSortedByInnerElements(): Mono<List<CourseDtoResponse>> =
        courseService.sortCoursesByInners()
            .collectList()
            .map { courses -> courseMapper.toDtoResponseList(courses) }

    @PostMapping
    fun createNewCourse(@Valid @RequestBody course: CourseDtoRequest): Mono<CourseDtoResponse> =
        courseService.create(courseMapper.toEntity(course))
            .map { createdCourse -> courseMapper.toDtoResponse(createdCourse) }

    @GetMapping("/{id}")
    fun getCourseById(@PathVariable id: String): Mono<CourseDtoResponse> =
        courseService.findById(id)
            .map { course -> courseMapper.toDtoResponse(course) }

    @PutMapping
    fun updateCourseById(@Valid @RequestBody course: CourseDtoRequest): Mono<CourseDtoResponse> =
        courseService.update(courseMapper.toEntityUpdate(course))
            .map { updated -> courseMapper.toDtoResponse(updated) }

    @DeleteMapping("/{id}")
    fun deleteCourseById(@PathVariable id: String): Mono<Void> =
        courseService.delete(id)

}
