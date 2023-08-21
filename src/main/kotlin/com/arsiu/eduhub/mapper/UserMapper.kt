package com.arsiu.eduhub.mapper

import com.arsiu.eduhub.dto.request.UserDtoRequest
import com.arsiu.eduhub.dto.response.UserDtoResponse
import com.arsiu.eduhub.model.User
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper(
    componentModel = "spring"
)
interface UserMapper {

    @Mapping(target = "id", ignore = true)
    fun toEntity(dto: UserDtoRequest): User

    fun toDtoOut(user: User): UserDtoResponse

    fun toDtoOutList(users: List<User>): List<UserDtoResponse>
}
