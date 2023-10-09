package com.arsiu.eduhub.user.application.rest

import com.arsiu.eduhub.user.application.dto.request.UserDtoRequest
import com.arsiu.eduhub.user.application.dto.response.UserDtoResponse
import com.arsiu.eduhub.user.application.mapper.UserMapper
import com.arsiu.eduhub.user.application.ports.UserService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/user")
class UserController(
    private val userService: UserService,
    private val userMapper: UserMapper
) {

    @GetMapping
    fun getAllUsers(): Mono<List<UserDtoResponse>> =
        userService.findAll()
            .collectList()
            .map { users -> userMapper.toDtoResponseList(users) }

    @PostMapping
    fun createNewUser(@Valid @RequestBody user: UserDtoRequest): Mono<UserDtoResponse> =
        userService.create(userMapper.toEntity(user))
            .map { createdUser -> userMapper.toDtoResponse(createdUser) }

    @GetMapping("/{id}")
    fun getUserById(@PathVariable id: String): Mono<UserDtoResponse> =
        userService.findById(id)
            .map { user -> userMapper.toDtoResponse(user) }

    @PutMapping
    fun updateUserById(@Valid @RequestBody user: UserDtoRequest): Mono<UserDtoResponse> =
        userService.update(userMapper.toEntityWithId(user))
            .map { updated -> userMapper.toDtoResponse(updated) }

    @DeleteMapping("/{id}")
    fun deletePostById(@PathVariable id: String): Mono<Void> =
        userService.delete(id)

}
