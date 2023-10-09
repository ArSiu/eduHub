package com.arsiu.eduhub.course.application.dto.request

import com.arsiu.eduhub.chapter.application.dto.request.ChapterDtoRequest
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Size

data class CourseDtoRequest(

    val id: String,

    @field:Size(
        min = 5,
        message = "name must be at least 5 characters long"
    )
    @field:NotEmpty(message = "name is required")
    val name: String,

    @field:NotEmpty(message = "ownerId is required")
    val ownerId: String,

    @field:NotEmpty(message = "chapters is required")
    val chapters: List<ChapterDtoRequest>

)
