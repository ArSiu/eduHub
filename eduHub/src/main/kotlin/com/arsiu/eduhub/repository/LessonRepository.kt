package com.arsiu.eduhub.repository

import com.arsiu.eduhub.model.Lesson
import org.springframework.stereotype.Repository

@Repository
interface LessonRepository : CascadeRepository<Lesson, String>
