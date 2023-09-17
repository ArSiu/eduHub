package com.arsiu.eduhub.mapper

import com.arsiu.eduhub.dto.request.AssignmentDtoRequest
import com.arsiu.eduhub.dto.response.AssignmentDtoResponse
import com.arsiu.eduhub.model.Assignment
import com.arsiu.eduhub.v2.assignmentsvc.commonmodels.assignment.AssignmentProto
import org.mapstruct.IterableMapping
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Named

@Mapper(componentModel = "spring")
interface AssignmentRestMapper {

    @Named("toEntity")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "lesson", ignore = true)
    fun toEntity(dto: AssignmentDtoRequest): Assignment

    @Named("toEntityList")
    @IterableMapping(qualifiedByName = ["toEntity"])
    fun toEntityList(dto: List<AssignmentDtoRequest>): List<Assignment>

    @Named("toEntityUpdate")
    @Mapping(target = "lesson", ignore = true)
    fun toEntityUpdate(dto: AssignmentDtoRequest): Assignment

    @Named("toEntityListUpdate")
    @IterableMapping(qualifiedByName = ["toEntityUpdate"])
    fun toEntityListUpdate(dto: List<AssignmentDtoRequest>): List<Assignment>

    fun toResponseDto(assignment: Assignment): AssignmentDtoResponse

    fun toResponseDtoList(assignments: List<Assignment>): List<AssignmentDtoResponse>

}

@Mapper(componentModel = "spring")
interface AssignmentNatsMapper {

    @Named("toEntity")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "lesson", ignore = true)
    fun toEntity(dto: AssignmentProto.Assignment): Assignment

    @Named("toEntityList")
    @IterableMapping(qualifiedByName = ["toEntity"])
    fun toEntityList(dto: List<AssignmentProto.Assignment>): List<Assignment>

    @Named("toEntityUpdate")
    @Mapping(target = "lesson", ignore = true)
    fun toEntityUpdate(dto: AssignmentProto.Assignment): Assignment

    @Named("toEntityListUpdate")
    @IterableMapping(qualifiedByName = ["toEntityUpdate"])
    fun toEntityListUpdate(dto: List<AssignmentProto.Assignment>): List<Assignment>

    fun toResponseDto(assignment: Assignment): AssignmentProto.Assignment

    fun toResponseDtoList(assignments: List<Assignment>): List<AssignmentProto.Assignment>

}