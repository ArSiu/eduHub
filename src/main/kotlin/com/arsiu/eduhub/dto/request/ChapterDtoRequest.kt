package com.arsiu.eduhub.dto.request

import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Size

data class ChapterDtoRequest(

    @field:NotEmpty(message = "name is required")
    @field:Size(min = 5, message = "name must be at least 5 characters long")
    val name: String,

    @field:NotEmpty(message = "courseId is required")
    val courseId: String,

    @field:NotEmpty(message = "lessons is required")
    val lessons: List<LessonDtoRequest>

)
