package com.arsiu.eduhub.dto.request

data class ChapterDtoRequest(
    val name: String,
    val courseId: Long,
    val lessons: List<LessonDtoRequest>
)
