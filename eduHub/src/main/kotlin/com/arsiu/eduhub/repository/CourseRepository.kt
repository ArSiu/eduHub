package com.arsiu.eduhub.repository

import com.arsiu.eduhub.model.Course
import com.arsiu.eduhub.repository.custom.CourseCustomRepository
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface CourseRepository : MongoRepository<Course, String>, CourseCustomRepository
