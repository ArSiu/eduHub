package com.arsiu.eduhub.lesson.infrastructure.mapper

import com.arsiu.eduhub.assignment.infrastructure.mapper.AssignmentRestMapper
import com.arsiu.eduhub.lesson.infrastructure.dto.request.LessonDtoRequest
import com.arsiu.eduhub.lesson.infrastructure.dto.response.LessonDtoResponse
import com.arsiu.eduhub.lesson.domain.Lesson
import com.arsiu.eduhub.lesson.infrastructure.persistence.entity.MongoLesson
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
    @Mapping(
        target = "assignments",
        source = "assignments",
        qualifiedByName = ["toEntityListUpdate"]
    )
    fun toEntityUpdate(dto: LessonDtoRequest): Lesson

    @Named("toEntityListUpdate")
    @IterableMapping(qualifiedByName = ["toEntityUpdate"])
    fun toEntityListUpdate(dto: List<LessonDtoRequest>): List<Lesson>

    @Mapping(target = "assignments", source = "assignments")
    fun toDtoResponse(lesson: Lesson): LessonDtoResponse

    @Mapping(target = "assignments", source = "assignments")
    fun toDtoResponseList(lessons: List<Lesson>): List<LessonDtoResponse>

}

@Mapper(componentModel = "spring")
interface LessonToEntityMapper {

    @Named("toEntity")
    @Mapping(target = "id", ignore = true)
    fun toEntity(model: Lesson): MongoLesson

    @Named("toEntityWithId")
    @Mapping(target = "chapterId", ignore = true)
    fun toEntityWithId(model: Lesson): MongoLesson

    @Named("toModel")
    @Mapping(target = "assignments", ignore = true)
    fun toModel(entity: MongoLesson): Lesson

    @Named("toModelWithAssignments")
    fun toModelWithAssignments(entity: MongoLesson): Lesson

}
