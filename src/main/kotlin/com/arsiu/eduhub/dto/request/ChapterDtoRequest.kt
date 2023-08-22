package com.arsiu.eduhub.dto.request

import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.Size

data class ChapterDtoRequest(

    @field:NotEmpty(message = "name is required")
    @field:Size(min = 5, message = "name must be at least 5 characters long")
    val name: String,

    @field:Positive(message = "courseId must be greater than 0")
    val courseId: Long,

    @field:NotEmpty(message = "lessons is required")
    val lessons: List<LessonDtoRequest>

)
