package com.arsiu.eduhub.dto.request

data class LessonDtoRequest(
    val name: String,
    val chapterId: Long,
    val assignments: List<AssignmentDtoRequest>
)
