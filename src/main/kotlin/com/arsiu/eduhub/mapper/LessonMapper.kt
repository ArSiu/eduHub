package com.arsiu.eduhub.mapper

import com.arsiu.eduhub.dto.request.LessonDtoRequest
import com.arsiu.eduhub.dto.response.LessonDtoResponse
import com.arsiu.eduhub.model.Lesson
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper(
    componentModel = "spring",
    uses = [AssignmentMapper::class]
)
interface LessonMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "chapter.id", source = "chapterId")
    @Mapping(target = "assignments", source = "assignments")
    fun toEntity(dto: LessonDtoRequest): Lesson

    @Mapping(target = "chapterId", source = "chapter.id")
    @Mapping(target = "assignments", source = "assignments")
    fun toDtoResponse(lesson: Lesson): LessonDtoResponse

    fun toDtoResponseList(lessons: List<Lesson>): List<LessonDtoResponse>

}
