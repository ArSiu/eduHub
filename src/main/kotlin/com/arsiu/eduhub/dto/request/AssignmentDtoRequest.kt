package com.arsiu.eduhub.dto.request

import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Size

data class AssignmentDtoRequest(

    val id: String,

    @field:NotEmpty(message = "name is required")
    @field:Size(min = 5, message = "name must be at least 5 characters long")
    val name: String,

)
