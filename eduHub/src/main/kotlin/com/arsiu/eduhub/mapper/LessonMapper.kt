package com.arsiu.eduhub.mapper

import com.arsiu.eduhub.dto.request.LessonDtoRequest
import com.arsiu.eduhub.dto.response.LessonDtoResponse
import com.arsiu.eduhub.model.Lesson
import org.mapstruct.IterableMapping
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Named

@Mapper(
    componentModel = "spring",
    uses = [AssignmentRestMapper::class]
)
interface LessonMapper {

    @Named("toEntity")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "chapterId", ignore = true)
    @Mapping(target = "assignments", source = "assignments", qualifiedByName = ["toEntityList"])
    fun toEntity(dto: LessonDtoRequest): Lesson

    @Named("toEntityList")
    @IterableMapping(qualifiedByName = ["toEntity"])
    fun toEntityList(dto: List<LessonDtoRequest>): List<Lesson>

    @Named("toEntityUpdate")
    @Mapping(target = "chapterId", ignore = true)
    @Mapping(target = "assignments", source = "assignments", qualifiedByName = ["toEntityListUpdate"])
    fun toEntityUpdate(dto: LessonDtoRequest): Lesson

    @Named("toEntityListUpdate")
    @IterableMapping(qualifiedByName = ["toEntityUpdate"])
    fun toEntityListUpdate(dto: List<LessonDtoRequest>): List<Lesson>

    @Mapping(target = "assignments", source = "assignments")
    fun toDtoResponse(lesson: Lesson): LessonDtoResponse

    @Mapping(target = "assignments", source = "assignments")
    fun toDtoResponseList(lessons: List<Lesson>): List<LessonDtoResponse>

}
