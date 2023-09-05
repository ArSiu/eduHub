package com.arsiu.eduhub.mapper

import com.arsiu.eduhub.dto.request.ChapterDtoRequest
import com.arsiu.eduhub.dto.response.ChapterDtoResponse
import com.arsiu.eduhub.model.Chapter
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper(
    componentModel = "spring",
    uses = [AssignmentMapper::class, LessonMapper::class],
)
interface ChapterMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "course.id", source = "courseId")
    @Mapping(target = "lessons", source = "lessons")
    fun toEntity(dto: ChapterDtoRequest): Chapter

    @Mapping(target = "courseId", source = "course.id")
    @Mapping(target = "lessons", source = "lessons")
    fun toDtoResponse(chapter: Chapter): ChapterDtoResponse

    @Mapping(target = "courseId", source = "course.id")
    @Mapping(target = "lessons", source = "lessons")
    fun toDtoResponseList(chapters: List<Chapter>): List<ChapterDtoResponse>

}
