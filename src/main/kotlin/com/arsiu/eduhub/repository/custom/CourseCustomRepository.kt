package com.arsiu.eduhub.repository.custom

import com.arsiu.eduhub.model.Course

interface CourseCustomRepository : CascadeRepository<Course, String> {
    fun sortCoursesByInners(): List<Course>
}
