package com.arsiu.eduhub.service

import com.arsiu.eduhub.exception.NotFoundException
import com.arsiu.eduhub.model.User
import com.arsiu.eduhub.repository.UserRepository
import com.arsiu.eduhub.service.interfaces.UserServiceInterface
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserService @Autowired constructor(
    val userRepository: UserRepository
) : UserServiceInterface {

    override fun findAll(): List<User> = userRepository.findAll().toList()

    override fun findById(id: Long): User =
        userRepository.findById(id).orElseThrow { NotFoundException("User with ID $id not found") }

    override fun create(entity: User): User = userRepository.save(entity)

    override fun update(id: Long, entity: User) {
        entity.id = findById(id).id
        create(entity)
    }

    override fun delete(id: Long) = userRepository.deleteById(id)

}
