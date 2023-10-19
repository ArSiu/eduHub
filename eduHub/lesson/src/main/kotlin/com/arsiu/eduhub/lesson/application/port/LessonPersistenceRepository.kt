package com.arsiu.eduhub.lesson.application.port

import com.arsiu.eduhub.common.application.port.GeneralPersistenceRepository
import com.arsiu.eduhub.lesson.domain.Lesson

interface LessonPersistenceRepository : GeneralPersistenceRepository<Lesson, String>
