package com.arsiu.eduhub.chapter.application.dto.request

import com.arsiu.eduhub.lesson.application.dto.request.LessonDtoRequest
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Size

data class ChapterDtoRequest(

    val id: String,

    @field:Size(
        min = 5,
        message = "name must be at least 5 characters long"
    )
    @field:NotEmpty(message = "name is required")
    val name: String,

    @field:NotEmpty(message = "lessons is required")
    val lessons: List<LessonDtoRequest>

)
