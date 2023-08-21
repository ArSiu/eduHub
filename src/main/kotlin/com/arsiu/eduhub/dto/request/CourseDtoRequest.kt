package com.arsiu.eduhub.dto.request

data class CourseDtoRequest(
    val name: String,
    val ownerId: Long,
    val chapters: List<ChapterDtoRequest>
)
