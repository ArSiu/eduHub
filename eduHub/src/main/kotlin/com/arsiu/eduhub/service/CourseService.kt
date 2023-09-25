package com.arsiu.eduhub.service

import com.arsiu.eduhub.model.Course
import reactor.core.publisher.Flux

interface CourseService : GeneralService<Course, String> {

    fun sortCoursesByInners(): Flux<Course>

}
