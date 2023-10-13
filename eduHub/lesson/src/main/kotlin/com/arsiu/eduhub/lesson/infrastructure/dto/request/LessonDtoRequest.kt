package com.arsiu.eduhub.lesson.infrastructure.dto.request

import com.arsiu.eduhub.assignment.infrastructure.dto.request.AssignmentDtoRequest
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Size

data class LessonDtoRequest(

    val id: String,

    @field:Size(
        min = 5,
        message = "name must be at least 5 characters long"
    )
    @field:NotEmpty(message = "name is required")
    val name: String,

    @field:NotEmpty(message = "assignments is required")
    val assignments: List<AssignmentDtoRequest>

)
