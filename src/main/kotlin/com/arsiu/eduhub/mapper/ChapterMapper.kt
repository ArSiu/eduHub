package com.arsiu.eduhub.mapper

import com.arsiu.eduhub.dto.request.ChapterDtoRequest
import com.arsiu.eduhub.dto.response.ChapterDtoResponse
import com.arsiu.eduhub.model.Chapter
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
    @Mapping(target = "course", ignore = true)
    @Mapping(target = "copy", ignore = true)
    @Mapping(target = "lessons", source = "lessons", qualifiedByName = ["toEntityList"])
    fun toEntity(dto: ChapterDtoRequest): Chapter

    @Named("toEntityList")
    @IterableMapping(qualifiedByName = ["toEntity"])
    fun toEntityList(dto: List<ChapterDtoRequest>): List<Chapter>

    @Named("toEntityUpdate")
    @Mapping(target = "course", ignore = true)
    @Mapping(target = "copy", ignore = true)
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
