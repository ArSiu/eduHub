package com.arsiu.eduhub.mapper

import com.arsiu.eduhub.dto.request.UserDtoRequest
import com.arsiu.eduhub.dto.response.UserDtoResponse
import com.arsiu.eduhub.model.User
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper(
    componentModel = "spring",
    uses = [CourseMapper::class]
)
interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "boughtCourses", ignore = true)
    @Mapping(target = "createdCourses", ignore = true)
    fun toEntity(dto: UserDtoRequest): User

    fun toDtoResponse(user: User): UserDtoResponse

    fun toDtoResponseList(users: List<User>): List<UserDtoResponse>

}