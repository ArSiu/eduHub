package com.arsiu.eduhub.dto.request

import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.Size

data class LessonDtoRequest(

    @field:NotEmpty(message = "name is required")
    @field:Size(min = 5, message = "name must be at least 5 characters long")
    val name: String,

    @field:Positive(message = "chapterId must be greater than 0")
    val chapterId: Long,

    @field:NotEmpty(message = "assignments is required")
    val assignments: List<AssignmentDtoRequest>

)
