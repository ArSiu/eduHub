package com.arsiu.eduhub.controller

import com.arsiu.eduhub.dto.request.CourseDtoRequest
import com.arsiu.eduhub.dto.response.CourseDtoResponse
import com.arsiu.eduhub.mapper.CourseMapper
import com.arsiu.eduhub.model.Course
import com.arsiu.eduhub.service.CourseService
import com.arsiu.eduhub.service.interfaces.CourseServiceInterface
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/course")
class CourseController(
    private val courseService: CourseServiceInterface,
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
    fun getCourseById(@PathVariable id: Long): ResponseEntity<CourseDtoResponse> =
        ResponseEntity(
            courseMapper.toDtoResponse(courseService.findById(id)),
            HttpStatus.OK
        )

    @PutMapping("/{id}")
    fun updateCourseById(
        @PathVariable id: Long,
        @Valid @RequestBody course: CourseDtoRequest
    ) {
        courseService.update(
            id,
            courseMapper.toEntity(course)
        )
    }

    @DeleteMapping("/{id}")
    fun deleteCourseById(@PathVariable id: Long) {
        courseService.delete(id)
    }

}
