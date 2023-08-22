package com.arsiu.eduhub.dto.request

import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Size

data class CourseDtoRequest(

    @field:NotEmpty(message = "name is required")
    @field:Size(min = 5, message = "name must be at least 5 characters long")
    val name: String,

    @field:NotEmpty(message = "ownerId is required")
    val ownerId: Long,

    @field:NotEmpty(message = "chapters is required")
    val chapters: List<ChapterDtoRequest>

)
