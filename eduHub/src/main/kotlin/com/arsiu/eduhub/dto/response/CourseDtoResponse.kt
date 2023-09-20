package com.arsiu.eduhub.dto.response

data class CourseDtoResponse(
    val id: String,
    val name: String,
    val ownerId: String,
    val chapters: List<ChapterDtoResponse>
)
