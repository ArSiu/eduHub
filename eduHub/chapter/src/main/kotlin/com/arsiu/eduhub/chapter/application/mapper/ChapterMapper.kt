package com.arsiu.eduhub.chapter.application.mapper

import com.arsiu.eduhub.chapter.application.dto.request.ChapterDtoRequest
import com.arsiu.eduhub.chapter.application.dto.response.ChapterDtoResponse
import com.arsiu.eduhub.chapter.domain.Chapter
import com.arsiu.eduhub.chapter.infrastructure.persistence.entity.ChapterEntity
import com.arsiu.eduhub.lesson.application.mapper.LessonMapper
import org.mapstruct.IterableMapping
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Named

@Mapper(
    componentModel = "spring",
    uses = [LessonMapper::class],
)
interface ChapterMapper {

    @Named("toEntity")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "courseId", ignore = true)
    @Mapping(target = "lessons", source = "lessons", qualifiedByName = ["toEntityList"])
    fun toEntity(dto: ChapterDtoRequest): Chapter

    @Named("toEntityList")
    @IterableMapping(qualifiedByName = ["toEntity"])
    fun toEntityList(dto: List<ChapterDtoRequest>): List<Chapter>

    @Named("toEntityUpdate")
    @Mapping(target = "courseId", ignore = true)
    @Mapping(target = "lessons", source = "lessons", qualifiedByName = ["toEntityListUpdate"])
    fun toEntityUpdate(dto: ChapterDtoRequest): Chapter

    @Named("toEntityListUpdate")
    @IterableMapping(qualifiedByName = ["toEntityUpdate"])
    fun toEntityListUpdate(dto: List<ChapterDtoRequest>): List<Chapter>

    @Mapping(target = "lessons", source = "lessons")
    fun toDtoResponse(chapter: Chapter): ChapterDtoResponse

    @Mapping(target = "lessons", source = "lessons")
    fun toDtoResponseList(chapters: List<Chapter>): List<ChapterDtoResponse>

}

@Mapper(componentModel = "spring")
interface ChapterToEntityMapper {

    @Named("toEntity")
    @Mapping(target = "id", ignore = true)
    fun toEntity(model: Chapter): ChapterEntity

    @Named("toEntityWithId")
    @Mapping(target = "courseId", ignore = true)
    fun toEntityWithId(model: Chapter): ChapterEntity

    fun toEntityList(model: List<Chapter>): List<ChapterEntity>

    @Named("toModel")
    @Mapping(target = "lessons", ignore = true)
    fun toModel(entity: ChapterEntity): Chapter

    @Named("toModelWithLessons")
    fun toModelWithLessons(entity: ChapterEntity): Chapter

    fun toModelList(entities: List<ChapterEntity>): List<Chapter>

}
