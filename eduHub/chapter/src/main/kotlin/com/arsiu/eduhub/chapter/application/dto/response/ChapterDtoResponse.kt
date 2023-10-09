package com.arsiu.eduhub.chapter.application.dto.response

import com.arsiu.eduhub.lesson.application.dto.response.LessonDtoResponse

data class ChapterDtoResponse(
    val id: String,
    val name: String,
    val lessons: List<LessonDtoResponse>
)
