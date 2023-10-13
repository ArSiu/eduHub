package com.arsiu.eduhub.course.infrastructure.dto.response

import com.arsiu.eduhub.chapter.infrastructure.dto.response.ChapterDtoResponse

data class CourseDtoResponse(
    val id: String,
    val name: String,
    val ownerId: String,
    val chapters: List<ChapterDtoResponse>
)
