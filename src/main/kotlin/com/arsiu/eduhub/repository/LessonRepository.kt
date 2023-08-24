package com.arsiu.eduhub.repository

import com.arsiu.eduhub.model.Lesson
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface LessonRepository : CrudRepository<Lesson, Long>
