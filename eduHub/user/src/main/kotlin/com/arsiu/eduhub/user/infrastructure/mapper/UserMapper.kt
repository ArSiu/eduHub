package com.arsiu.eduhub.user.infrastructure.mapper


import com.arsiu.eduhub.course.infrastructure.mapper.CourseMapper
import com.arsiu.eduhub.user.domain.User
import com.arsiu.eduhub.user.infrastructure.dto.request.UserDtoRequest
import com.arsiu.eduhub.user.infrastructure.dto.response.UserDtoResponse
import com.arsiu.eduhub.user.infrastructure.persistence.entity.MongoUser
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
    fun toEntity(model: User): MongoUser

    @Named("toEntityWithId")
    @Mapping(target = "createdCourses", ignore = true)
    @Mapping(target = "boughtCourses", ignore = true)
    fun toEntityWithId(model: User): MongoUser

    @Named("toModel")
    @Mapping(target = "createdCourses", ignore = true)
    @Mapping(target = "boughtCourses", ignore = true)
    fun toModel(entity: MongoUser): User

}
