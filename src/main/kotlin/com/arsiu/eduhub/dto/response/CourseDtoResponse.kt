package com.arsiu.eduhub.dto.response

data class CourseDtoResponse(
    val id: Long,
    val name: String,
    val ownerId: Long,
    val chapters: List<ChapterDtoResponse>
)
