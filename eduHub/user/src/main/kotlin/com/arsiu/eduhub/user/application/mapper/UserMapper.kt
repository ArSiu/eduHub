package com.arsiu.eduhub.user.application.mapper


import com.arsiu.eduhub.course.application.mapper.CourseMapper
import com.arsiu.eduhub.user.application.dto.request.UserDtoRequest
import com.arsiu.eduhub.user.application.dto.response.UserDtoResponse
import com.arsiu.eduhub.user.domain.User
import com.arsiu.eduhub.user.infrastructure.persistence.entity.UserEntity
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Named

@Mapper(
    componentModel = "spring",
    uses = [CourseMapper::class]
)
interface UserMapper {

    @Named("toEntity")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "boughtCourses", ignore = true)
    @Mapping(target = "createdCourses", ignore = true)
    fun toEntity(dto: UserDtoRequest): User

    @Named("toEntityWithId")
    @Mapping(target = "boughtCourses", ignore = true)
    @Mapping(target = "createdCourses", ignore = true)
    fun toEntityWithId(dto: UserDtoRequest): User

    fun toDtoResponse(user: User): UserDtoResponse

    fun toDtoResponseList(users: List<User>): List<UserDtoResponse>

}

@Mapper(componentModel = "spring")
interface UserToEntityMapper {

    @Named("toEntity")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdCourses", ignore = true)
    @Mapping(target = "boughtCourses", ignore = true)
    fun toEntity(model: User): UserEntity

    @Named("toEntityWithId")
    @Mapping(target = "createdCourses", ignore = true)
    @Mapping(target = "boughtCourses", ignore = true)
    fun toEntityWithId(model: User): UserEntity

    @Named("toModel")
    @Mapping(target = "createdCourses", ignore = true)
    @Mapping(target = "boughtCourses", ignore = true)
    fun toModel(entity: UserEntity): User

}
