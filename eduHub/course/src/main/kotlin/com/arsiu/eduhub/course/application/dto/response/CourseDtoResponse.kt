package com.arsiu.eduhub.course.application.dto.response

import com.arsiu.eduhub.chapter.application.dto.response.ChapterDtoResponse

data class CourseDtoResponse(
    val id: String,
    val name: String,
    val ownerId: String,
    val chapters: List<ChapterDtoResponse>
)
