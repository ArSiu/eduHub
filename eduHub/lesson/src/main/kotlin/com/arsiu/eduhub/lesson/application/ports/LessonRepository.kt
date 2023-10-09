package com.arsiu.eduhub.lesson.application.ports

import com.arsiu.eduhub.common.application.ports.repository.GeneralRepository
import com.arsiu.eduhub.lesson.domain.Lesson
import org.springframework.stereotype.Repository

@Repository
interface LessonRepository : GeneralRepository<Lesson, String>
