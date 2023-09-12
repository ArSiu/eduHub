package com.arsiu.eduhub.service.impl

import com.arsiu.eduhub.exception.NotFoundException
import com.arsiu.eduhub.model.User
import com.arsiu.eduhub.repository.UserRepository
import com.arsiu.eduhub.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserServiceImpl @Autowired constructor(
    private val userRepository: UserRepository
) : UserService {

    override fun findAll(): List<User> =
        userRepository.findAll().toList()

    override fun findById(id: String): User =
        userRepository.findById(id).orElseThrow { NotFoundException("User with ID $id not found") }

    override fun create(entity: User): User =
        userRepository.save(entity)

    override fun update(entity: User): User {
        findById(entity.id)
        return create(entity)
    }

    override fun delete(id: String) =
        userRepository.deleteById(id)

}
