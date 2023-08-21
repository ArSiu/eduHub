package com.arsiu.eduhub.dto.response

data class ChapterDtoResponse(
    val id: Long,
    val name: String,
    val courseId: Long,
    val lessons: List<LessonDtoResponse>
)
