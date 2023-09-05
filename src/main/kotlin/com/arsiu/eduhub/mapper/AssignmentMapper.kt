package com.arsiu.eduhub.mapper

import com.arsiu.eduhub.dto.request.AssignmentDtoRequest
import com.arsiu.eduhub.dto.response.AssignmentDtoResponse
import com.arsiu.eduhub.model.Assignment
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper(
    componentModel = "spring",
    uses = [LessonMapper::class]
)
interface AssignmentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "lesson.id", source = "lessonId")
    fun toEntity(dto: AssignmentDtoRequest): Assignment

    @Mapping(target = "lessonId", source = "lesson.id")
    fun toDtoResponse(assignment: Assignment): AssignmentDtoResponse

    @Mapping(target = "lessonId", source = "lesson.id")
    fun toDtoResponseList(assignments: List<Assignment>): List<AssignmentDtoResponse>


}
