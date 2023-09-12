package com.arsiu.eduhub.dto.response

import com.arsiu.eduhub.model.enums.Role

data class UserDtoResponse(
    val id: String,
    val firstName: String,
    val secondName: String,
    val role: Role,
    val boughtCourses: Set<CourseDtoResponse>,
    val createdCourses: Set<CourseDtoResponse>
)
