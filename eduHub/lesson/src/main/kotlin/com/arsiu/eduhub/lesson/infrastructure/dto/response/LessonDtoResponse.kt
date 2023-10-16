package com.arsiu.eduhub.lesson.infrastructure.dto.response

import com.arsiu.eduhub.assignment.infrastructure.dto.response.AssignmentDtoResponse

data class LessonDtoResponse(
    val id: String,
    val name: String,
    val assignments: List<AssignmentDtoResponse>
)
