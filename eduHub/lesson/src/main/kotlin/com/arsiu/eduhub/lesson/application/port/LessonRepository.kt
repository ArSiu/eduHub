package com.arsiu.eduhub.lesson.application.port

import com.arsiu.eduhub.common.application.port.repository.GeneralRepository
import com.arsiu.eduhub.lesson.domain.Lesson
import org.springframework.stereotype.Repository

@Repository
interface LessonRepository : GeneralRepository<Lesson, String>
