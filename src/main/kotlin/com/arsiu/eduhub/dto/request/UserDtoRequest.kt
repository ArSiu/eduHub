package com.arsiu.eduhub.dto.request

import com.arsiu.eduhub.anotation.ValueOfEnum
import com.arsiu.eduhub.model.enums.Role
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Size

data class UserDtoRequest(

    @field:NotEmpty(message = "First name is required")
    val firstName: String,

    @field:NotEmpty(message = "Last name is required")
    val secondName: String,

    @field:Email(message = "Invalid email format")
    @field:NotEmpty(message = "Email is required")
    val email: String,

    @field:Size(min = 6, message = "Password must be at least 6 characters long")
    @field:NotEmpty(message = "Password is required")
    val password: String,

    @field:ValueOfEnum(enumClass = Role::class, message = "Valid values are: ADMIN, MODERATOR, TEACHER, STUDENT")
    val role: Role

)
