package com.arsiu.eduhub.mapper

import com.arsiu.eduhub.dto.request.CourseDtoRequest
import com.arsiu.eduhub.dto.response.CourseDtoResponse
import com.arsiu.eduhub.model.Course
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
    @Mapping(target = "copy", ignore = true)
    @Mapping(target = "owner.id", source = "ownerId")
    @Mapping(target = "chapters", source = "chapters", qualifiedByName = ["toEntityList"])
    fun toEntity(dto: CourseDtoRequest): Course

    @Named("toEntityList")
    @IterableMapping(qualifiedByName = ["toEntity"])
    fun toEntityList(dto: List<CourseDtoRequest>): List<Course>

    @Named("toEntityUpdate")
    @Mapping(target = "students", ignore = true)
    @Mapping(target = "copy", ignore = true)
    @Mapping(target = "owner.id", source = "ownerId")
    @Mapping(target = "chapters", source = "chapters", qualifiedByName = ["toEntityListUpdate"])
    fun toEntityUpdate(dto: CourseDtoRequest): Course

    @Named("toEntityListUpdate")
    @IterableMapping(qualifiedByName = ["toEntityUpdate"])
    fun toEntityListUpdate(dto: List<CourseDtoRequest>): List<Course>

    @Mapping(target = "ownerId", source = "owner.id")
    @Mapping(target = "chapters", source = "chapters")
    fun toDtoResponse(course: Course): CourseDtoResponse

    @Mapping(target = "ownerId", source = "owner.id")
    @Mapping(target = "chapters", source = "chapters")
    fun toDtoResponseList(courses: List<Course>): List<CourseDtoResponse>

}
