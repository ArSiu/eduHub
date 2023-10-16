package com.arsiu.eduhub.chapter.infrastructure.dto.response

import com.arsiu.eduhub.lesson.infrastructure.dto.response.LessonDtoResponse

data class ChapterDtoResponse(
    val id: String,
    val name: String,
    val lessons: List<LessonDtoResponse>
)
