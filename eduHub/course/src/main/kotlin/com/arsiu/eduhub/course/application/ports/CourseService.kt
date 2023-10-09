package com.arsiu.eduhub.course.application.ports

import com.arsiu.eduhub.common.application.ports.service.GeneralService
import com.arsiu.eduhub.course.domain.Course
import reactor.core.publisher.Flux

interface CourseService : GeneralService<Course, String> {

    fun sortCoursesByInners(): Flux<Course>

}
