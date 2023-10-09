package com.arsiu.eduhub.lesson.application.dto.response

import com.arsiu.eduhub.assignment.application.dto.response.AssignmentDtoResponse

data class LessonDtoResponse(
    val id: String,
    val name: String,
    val assignments: List<AssignmentDtoResponse>
)
