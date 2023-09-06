package com.arsiu.eduhub.controller

import com.arsiu.eduhub.dto.request.UserDtoRequest
import com.arsiu.eduhub.dto.response.UserDtoResponse
import com.arsiu.eduhub.mapper.UserMapper
import com.arsiu.eduhub.model.User
import com.arsiu.eduhub.service.UserService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/user")
class UserController(
    private val userService: UserService,
    private val userMapper: UserMapper
) {

    @GetMapping
    fun getAllUsers(): List<UserDtoResponse> =
        userMapper.toDtoResponseList(userService.findAll())

    @PostMapping
    fun createNewUser(@Valid @RequestBody user: UserDtoRequest): UserDtoResponse {
        val createdUser: User = userService.create(userMapper.toEntity(user))
        return userMapper.toDtoResponse(createdUser)
    }

    @GetMapping("/{id}")
    fun getUserById(@PathVariable id: String): UserDtoResponse =
        userMapper.toDtoResponse(userService.findById(id))

    @PutMapping
    fun updateUserById(@Valid @RequestBody user: UserDtoRequest): UserDtoResponse {
        val updated = userService.update(userMapper.toEntity(user))
        return userMapper.toDtoResponse(updated)
    }

    @DeleteMapping("/{id}")
    fun deletePostById(@PathVariable id: String) =
        userService.delete(id)

}
