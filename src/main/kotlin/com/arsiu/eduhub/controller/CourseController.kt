package com.arsiu.eduhub.controller

import com.arsiu.eduhub.dto.request.CourseDtoRequest
import com.arsiu.eduhub.dto.response.CourseDtoResponse
import com.arsiu.eduhub.mapper.CourseMapper
import com.arsiu.eduhub.model.Course
import com.arsiu.eduhub.service.CourseService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/course")
class CourseController(
    private val courseService: CourseService,
    private val courseMapper: CourseMapper
) {
    @GetMapping
    fun getAllCourses(): ResponseEntity<List<CourseDtoResponse>> =
        ResponseEntity(
            courseMapper.toDtoResponseList(courseService.findAll()),
            HttpStatus.OK
        )

    @PostMapping
    fun createNewCourse(@Valid @RequestBody course: CourseDtoRequest): ResponseEntity<CourseDtoResponse> {
        val createdCourse: Course = courseService.create(courseMapper.toEntity(course))
        return ResponseEntity(
            courseMapper.toDtoResponse(createdCourse),
            HttpStatus.CREATED
        )
    }

    @GetMapping("/{id}")
    fun getCourseById(@PathVariable(value = "id") id: Long): ResponseEntity<CourseDtoResponse> =
        ResponseEntity(
            courseMapper.toDtoResponse(courseService.findById(id)),
            HttpStatus.OK
        )

    @PutMapping("/{id}")
    fun updateCourseById(
        @PathVariable(value = "id") id: Long,
        @Valid @RequestBody course: CourseDtoRequest
    ): ResponseEntity<Void> {
        courseService.update(
            id,
            courseMapper.toEntity(course)
        )
        return ResponseEntity(HttpStatus.OK)
    }

    @DeleteMapping("/{id}")
    fun deleteCourseById(@PathVariable(value = "id") id: Long): ResponseEntity<Void> {
        courseService.delete(id)
        return ResponseEntity(HttpStatus.OK)
    }
}
