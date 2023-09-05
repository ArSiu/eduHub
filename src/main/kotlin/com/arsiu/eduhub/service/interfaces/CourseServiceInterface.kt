package com.arsiu.eduhub.service.interfaces

import com.arsiu.eduhub.model.Course

interface CourseServiceInterface : GeneralServiceInterface<Course, String> {
    fun sortCoursesByInners(): List<Course>
}
