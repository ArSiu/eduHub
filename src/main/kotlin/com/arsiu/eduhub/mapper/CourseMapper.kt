package com.arsiu.eduhub.mapper

import com.arsiu.eduhub.dto.request.CourseDtoRequest
import com.arsiu.eduhub.dto.response.CourseDtoResponse
import com.arsiu.eduhub.model.Course
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper(
    componentModel = "spring",
    uses = [AssignmentMapper::class, LessonMapper::class, ChapterMapper::class]
)
interface CourseMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "students", ignore = true)
    @Mapping(target = "chapters", source = "chapters")
    @Mapping(target = "owner.id", source = "ownerId")
    fun toEntity(dto: CourseDtoRequest): Course

    @Mapping(target = "ownerId", source = "owner.id")
    @Mapping(target = "chapters", source = "chapters")
    fun toDtoResponse(course: Course): CourseDtoResponse

    @Mapping(target = "ownerId", source = "owner.id")
    @Mapping(target = "chapters", source = "chapters")
    fun toDtoResponseList(courses: List<Course>): List<CourseDtoResponse>

}
