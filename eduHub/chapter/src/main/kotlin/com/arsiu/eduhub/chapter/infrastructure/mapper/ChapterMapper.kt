package com.arsiu.eduhub.chapter.infrastructure.mapper

import com.arsiu.eduhub.chapter.infrastructure.dto.request.ChapterDtoRequest
import com.arsiu.eduhub.chapter.infrastructure.dto.response.ChapterDtoResponse
import com.arsiu.eduhub.chapter.domain.Chapter
import com.arsiu.eduhub.chapter.infrastructure.persistence.entity.MongoChapter
import com.arsiu.eduhub.lesson.infrastructure.mapper.LessonMapper
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
    fun toEntity(model: Chapter): MongoChapter

    @Named("toEntityWithId")
    @Mapping(target = "courseId", ignore = true)
    fun toEntityWithId(model: Chapter): MongoChapter

    fun toEntityList(model: List<Chapter>): List<MongoChapter>

    @Named("toModel")
    @Mapping(target = "lessons", ignore = true)
    fun toModel(entity: MongoChapter): Chapter

    @Named("toModelWithLessons")
    fun toModelWithLessons(entity: MongoChapter): Chapter

    fun toModelList(entities: List<MongoChapter>): List<Chapter>

}
