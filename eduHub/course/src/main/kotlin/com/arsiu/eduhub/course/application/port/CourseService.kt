package com.arsiu.eduhub.course.application.port

import com.arsiu.eduhub.common.application.port.GeneralService
import com.arsiu.eduhub.course.domain.Course
import reactor.core.publisher.Flux

interface CourseService : GeneralService<Course, String> {

    fun sortCoursesByInners(): Flux<Course>

}
