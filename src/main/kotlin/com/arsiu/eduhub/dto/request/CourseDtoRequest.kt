package com.arsiu.eduhub.dto.request

import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class CourseDtoRequest(

    @field:NotEmpty(message = "name is required")
    @field:Size(min = 5, message = "name must be at least 5 characters long")
    @NotNull(message = "Name is required")
    val name: String,

    @field:NotEmpty(message = "ownerId is required")
    val ownerId: String,

    @field:NotEmpty(message = "chapters is required")
    val chapters: List<ChapterDtoRequest>

)
