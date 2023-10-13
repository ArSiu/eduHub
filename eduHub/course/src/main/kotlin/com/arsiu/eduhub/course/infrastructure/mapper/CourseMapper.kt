package com.arsiu.eduhub.course.infrastructure.mapper

import com.arsiu.eduhub.chapter.infrastructure.mapper.ChapterMapper
import com.arsiu.eduhub.course.infrastructure.dto.request.CourseDtoRequest
import com.arsiu.eduhub.course.infrastructure.dto.response.CourseDtoResponse
import com.arsiu.eduhub.course.domain.Course
import com.arsiu.eduhub.course.infrastructure.persistence.entity.MongoCourse
import org.mapstruct.IterableMapping
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Named

@Mapper(
    componentModel = "spring",
    uses = [ChapterMapper::class]
)
interface CourseMapper {

    @Named("toEntity")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "students", ignore = true)
    @Mapping(target = "ownerId", source = "ownerId")
    @Mapping(target = "chapters", source = "chapters", qualifiedByName = ["toEntityList"])
    fun toEntity(dto: CourseDtoRequest): Course

    @Named("toEntityList")
    @IterableMapping(qualifiedByName = ["toEntity"])
    fun toEntityList(dto: List<CourseDtoRequest>): List<Course>

    @Named("toEntityUpdate")
    @Mapping(target = "students", ignore = true)
    @Mapping(target = "ownerId", source = "ownerId")
    @Mapping(target = "chapters", source = "chapters", qualifiedByName = ["toEntityListUpdate"])
    fun toEntityUpdate(dto: CourseDtoRequest): Course

    @Named("toEntityListUpdate")
    @IterableMapping(qualifiedByName = ["toEntityUpdate"])
    fun toEntityListUpdate(dto: List<CourseDtoRequest>): List<Course>

    @Mapping(target = "ownerId", source = "ownerId")
    @Mapping(target = "chapters", source = "chapters")
    fun toDtoResponse(course: Course): CourseDtoResponse

    @Mapping(target = "ownerId", source = "ownerId")
    @Mapping(target = "chapters", source = "chapters")
    fun toDtoResponseList(courses: List<Course>): List<CourseDtoResponse>

}

@Mapper(componentModel = "spring")
interface CourseToEntityMapper {

    @Named("toEntity")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "students", ignore = true)
    fun toEntity(model: Course): MongoCourse

    @Named("toEntityWithId")
    @Mapping(target = "students", ignore = true)
    fun toEntityWithId(model: Course): MongoCourse

    @Named("toModel")
    @Mapping(target = "students", ignore = true)
    @Mapping(target = "chapters", ignore = true)
    fun toModel(entity: MongoCourse): Course

    @Named("toModelWithChapters")
    @Mapping(target = "students", ignore = true)
    fun toModelWithChapters(entity: MongoCourse): Course

}
