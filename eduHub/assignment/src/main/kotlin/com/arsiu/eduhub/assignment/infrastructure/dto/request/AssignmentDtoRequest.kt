package com.arsiu.eduhub.assignment.infrastructure.dto.request

import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Size

data class AssignmentDtoRequest(

    val id: String,

    @field:Size(
        min = 5,
        message = "name must be at least 5 characters long"
    )
    @field:NotEmpty(message = "name is required")
    val name: String,

)
