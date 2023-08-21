package com.arsiu.eduhub.controller

import com.arsiu.eduhub.dto.request.UserDtoRequest
import com.arsiu.eduhub.dto.response.UserDtoResponse
import com.arsiu.eduhub.mapper.UserMapper
import com.arsiu.eduhub.model.User
import com.arsiu.eduhub.service.UserService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/user")
class UserController(private val userService: UserService, private val userMapper: UserMapper) {

    @GetMapping
    fun getAllUsers(): ResponseEntity<List<UserDtoResponse>> =
        ResponseEntity(
            userMapper.toDtoOutList(userService.findAll()),
            HttpStatus.OK
        )

    @PostMapping
    fun createNewUser(@Valid @RequestBody user: UserDtoRequest): ResponseEntity<UserDtoResponse> {
        val createdUser: User = userService.create(userMapper.toEntity(user))
        return ResponseEntity(userMapper.toDtoOut(createdUser), HttpStatus.CREATED)
    }

    @GetMapping("/{id}")
    fun getUserById(@PathVariable(value = "id") id: Long): ResponseEntity<UserDtoResponse> =
        ResponseEntity(
            userMapper.toDtoOut(userService.findById(id)),
            HttpStatus.OK
        )

    @PutMapping("/{id}")
    fun updateUserById(
        @PathVariable(value = "id") id: Long,
        @Valid @RequestBody user: UserDtoRequest
    ): ResponseEntity<Void> {
        userService.update(
            id,
            userMapper.toEntity(user)
        )
        return ResponseEntity(HttpStatus.OK)
    }

    @DeleteMapping("/{id}")
    fun deletePostById(@PathVariable(value = "id") id: Long): ResponseEntity<Void> {
        userService.delete(id)
        return ResponseEntity(HttpStatus.OK)
    }
}
