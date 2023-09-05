package com.arsiu.eduhub.controller

import com.arsiu.eduhub.dto.request.UserDtoRequest
import com.arsiu.eduhub.dto.response.UserDtoResponse
import com.arsiu.eduhub.mapper.UserMapper
import com.arsiu.eduhub.model.User
import com.arsiu.eduhub.service.interfaces.UserServiceInterface
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
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
    private val userService: UserServiceInterface,
    private val userMapper: UserMapper
) {

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
    fun getUserById(@PathVariable id: String): ResponseEntity<UserDtoResponse> =
        ResponseEntity(
            userMapper.toDtoOut(userService.findById(id)),
            HttpStatus.OK
        )

    @PutMapping("/{id}")
    fun updateUserById(
        @PathVariable id: String,
        @Valid @RequestBody user: UserDtoRequest
    ) {
        userService.update(
            id,
            userMapper.toEntity(user)
        )
    }

    @DeleteMapping("/{id}")
    fun deletePostById(@PathVariable id: String) {
        userService.delete(id)
    }

}
