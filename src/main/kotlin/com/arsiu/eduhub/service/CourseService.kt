package com.arsiu.eduhub.service

import com.arsiu.eduhub.model.Course

interface CourseService : GeneralService<Course, String> {

    fun sortCoursesByInners(): List<Course>

}
