package com.arsiu.eduhub.service.impl

import com.arsiu.eduhub.exception.NotFoundException
import com.arsiu.eduhub.model.User
import com.arsiu.eduhub.repository.UserRepository
import com.arsiu.eduhub.service.UserService
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class UserServiceImpl(
    private val userRepository: UserRepository
) : UserService {

    override fun findAll(): Flux<User> =
        userRepository.findAll()

    override fun findById(id: String): Mono<User> =
        userRepository.findById(id)
            .switchIfEmpty(Mono.error(NotFoundException("User with ID $id not found")))

    override fun create(entity: User): Mono<User> =
        userRepository.save(entity)

    override fun update(entity: User): Mono<User> =
        findById(entity.id).then(create(entity))

    override fun delete(id: String): Mono<Void> =
        userRepository.deleteById(id)
}
