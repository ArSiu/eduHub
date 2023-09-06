package com.arsiu.eduhub.dto.response

data class LessonDtoResponse(
    val id: String,
    val name: String,
    val assignments: List<AssignmentDtoResponse>
)
