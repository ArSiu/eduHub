package com.arsiu.eduhub.repository

import com.arsiu.eduhub.model.Course

import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux

@Repository
interface CourseRepository : CascadeRepository<Course, String> {

    fun sortCoursesByInners(): Flux<Course>

}
