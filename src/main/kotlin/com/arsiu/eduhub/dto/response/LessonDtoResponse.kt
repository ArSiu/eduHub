package com.arsiu.eduhub.dto.response

data class LessonDtoResponse(
    val id: Long,
    val name: String,
    val chapterId: Long,
    val assignments: List<AssignmentDtoResponse>?
)
