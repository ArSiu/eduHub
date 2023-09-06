package com.arsiu.eduhub.controller

import com.arsiu.eduhub.dto.request.CourseDtoRequest
import com.arsiu.eduhub.dto.response.CourseDtoResponse
import com.arsiu.eduhub.mapper.CourseMapper
import com.arsiu.eduhub.model.Course
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

@RestController
@RequestMapping("/api/course")
class CourseController(
    private val courseService: CourseService,
    private val courseMapper: CourseMapper
) {

    @GetMapping
    fun getAllCourses(): List<CourseDtoResponse> =
        courseMapper.toDtoResponseList(courseService.findAll())

    @GetMapping("/mostElements")
    fun getAllCoursesSortedByInnerElements(): List<CourseDtoResponse> =
        courseMapper.toDtoResponseList(courseService.sortCoursesByInners())

    @PostMapping
    fun createNewCourse(@Valid @RequestBody course: CourseDtoRequest): CourseDtoResponse {
        val createdCourse: Course = courseService.create(courseMapper.toEntity(course))
        return courseMapper.toDtoResponse(createdCourse)
    }

    @GetMapping("/{id}")
    fun getCourseById(@PathVariable id: String): CourseDtoResponse =
        courseMapper.toDtoResponse(courseService.findById(id))

    @PutMapping
    fun updateCourseById(@Valid @RequestBody course: CourseDtoRequest): CourseDtoResponse {
        val updated = courseService.update(courseMapper.toEntityUpdate(course))
        return courseMapper.toDtoResponse(updated)
    }

    @DeleteMapping("/{id}")
    fun deleteCourseById(@PathVariable id: String) =
        courseService.delete(id)

}
