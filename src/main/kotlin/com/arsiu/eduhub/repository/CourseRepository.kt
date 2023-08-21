package com.arsiu.eduhub.repository

import com.arsiu.eduhub.model.Course
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface CourseRepository : CrudRepository<Course, Long>
