package com.arsiu.eduhub.repository.custom

import com.arsiu.eduhub.model.Lesson

interface LessonCustomRepository : CascadeRepository<Lesson, String>
