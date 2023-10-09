package com.arsiu.eduhub.assignment.application.mapper

import com.arsiu.eduhub.assignment.application.dto.request.AssignmentDtoRequest
import com.arsiu.eduhub.assignment.application.dto.response.AssignmentDtoResponse
import com.arsiu.eduhub.assignment.domain.Assignment
import com.arsiu.eduhub.assignment.infrastructure.persistence.entity.AssignmentEntity
import com.arsiu.eduhub.v2.assignmentsvc.commonmodels.assignment.AssignmentProto
import org.mapstruct.BeanMapping
import org.mapstruct.IterableMapping
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Named

@Mapper(componentModel = "spring")
interface AssignmentRestMapper {

    @Named("toEntity")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "lessonId", ignore = true)
    fun toEntity(dto: AssignmentDtoRequest): Assignment

    @Named("toEntityList")
    @IterableMapping(qualifiedByName = ["toEntity"])
    fun toEntityList(dto: List<AssignmentDtoRequest>): List<Assignment>

    @Named("toEntityUpdate")
    @Mapping(target = "lessonId", ignore = true)
    fun toEntityUpdate(dto: AssignmentDtoRequest): Assignment

    @Named("toEntityListUpdate")
    @IterableMapping(qualifiedByName = ["toEntityUpdate"])
    fun toEntityListUpdate(dto: List<AssignmentDtoRequest>): List<Assignment>

    fun toResponseDto(assignment: Assignment): AssignmentDtoResponse

    fun toResponseDtoList(assignments: List<Assignment>): List<AssignmentDtoResponse>

}

@Mapper(componentModel = "spring")
interface AssignmentToEntityMapper {

    @Named("toEntity")
    @Mapping(target = "id", ignore = true)
    fun toEntity(model: Assignment): AssignmentEntity

    @Named("toEntityWithId")
    @Mapping(target = "lessonId", ignore = true)
    fun toEntityWithId(model: Assignment): AssignmentEntity

    fun toEntityList(model: List<Assignment>): List<AssignmentEntity>

    fun toModel(entity: AssignmentEntity): Assignment

    fun toModelList(entities: List<AssignmentEntity>): List<Assignment>

}

@Mapper(componentModel = "spring")
interface AssignmentProtoMapper {

    @Named("toEntity")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "lessonId", ignore = true)
    fun toEntity(dto: AssignmentProto): Assignment

    @Named("toEntityList")
    @IterableMapping(qualifiedByName = ["toEntity"])
    fun toEntityList(dto: List<AssignmentProto>): List<Assignment>

    @Named("toEntityUpdate")
    @Mapping(target = "lessonId", ignore = true)
    fun toEntityUpdate(dto: AssignmentProto): Assignment

    @Named("toEntityListUpdate")
    @IterableMapping(qualifiedByName = ["toEntityUpdate"])
    fun toEntityListUpdate(dto: List<AssignmentProto>): List<Assignment>

    @Named("toResponseDto")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    fun toResponseDto(assignment: Assignment): AssignmentProto

    @Named("toResponseDtoList")
    @IterableMapping(qualifiedByName = ["toResponseDto"])
    fun toResponseDtoList(assignments: List<Assignment>): List<AssignmentProto>

}
