package com.arsiu.eduhub.dto.response

data class ChapterDtoResponse(
    val id: String,
    val name: String,
    val lessons: List<LessonDtoResponse>
)
