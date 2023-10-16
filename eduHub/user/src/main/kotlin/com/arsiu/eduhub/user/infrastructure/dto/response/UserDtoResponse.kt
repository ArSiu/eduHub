package com.arsiu.eduhub.user.infrastructure.dto.response

import com.arsiu.eduhub.user.domain.Role

data class UserDtoResponse(
    val id: String,
    val firstName: String,
    val secondName: String,
    val role: Role
)
