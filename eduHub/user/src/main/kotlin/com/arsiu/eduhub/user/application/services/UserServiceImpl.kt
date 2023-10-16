package com.arsiu.eduhub.user.application.services

import com.arsiu.eduhub.common.application.exception.NotFoundException
import com.arsiu.eduhub.user.application.port.UserMongoRepository
import com.arsiu.eduhub.user.application.port.UserService
import com.arsiu.eduhub.user.domain.User
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class UserServiceImpl(
    private val userRepository: UserMongoRepository
) : UserService {

    override fun findAll(): Flux<User> =
        userRepository.findAll()

    override fun findById(id: String): Mono<User> =
        userRepository.findById(id)
            .switchIfEmpty(Mono.error(NotFoundException("User with ID $id not found")))

    override fun create(entity: User): Mono<User> =
        userRepository.save(entity)

    override fun update(entity: User): Mono<User> =
        findById(entity.id).then(userRepository.upsert(entity))

    override fun delete(id: String): Mono<Void> =
        findById(id).flatMap { userRepository.remove(it) }

}
