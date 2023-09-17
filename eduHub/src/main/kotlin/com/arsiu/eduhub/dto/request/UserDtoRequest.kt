package com.arsiu.eduhub.dto.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class UserDtoRequest(

    val id: String,

    @field:NotEmpty(message = "firstName is required")
    @field:Size(min = 3, message = "firstName must be at least 3 characters long")
    val firstName: String,

    @field:NotEmpty(message = "secondName is required")
    @field:Size(min = 3, message = "secondName must be at least 3 characters long")
    val secondName: String,

    @field:Email(message = "Invalid email format")
    @field:NotEmpty(message = "email is required")
    val email: String,

    @field:Size(min = 6, message = "password must be at least 6 characters long")
    @field:NotEmpty(message = "password is required")
    val password: String,

    @field:Pattern(
        regexp = "ADMIN|MODERATOR|TEACHER|STUDENT",
        message = "role must match ADMIN|MODERATOR|TEACHER|STUDENT"
    )
    val role: String

)
