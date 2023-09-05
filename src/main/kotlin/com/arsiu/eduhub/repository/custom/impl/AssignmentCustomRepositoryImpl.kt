package com.arsiu.eduhub.repository.custom.impl

import com.arsiu.eduhub.model.Assignment
import com.arsiu.eduhub.repository.custom.AssignmentCustomRepository
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.stereotype.Repository

@Repository
class AssignmentCustomRepositoryImpl(
    private val mongoTemplate: MongoTemplate
) : AssignmentCustomRepository {

    override fun createCascade(entity: Assignment): Assignment = mongoTemplate.save(entity)

    override fun deleteCascade(entity: Assignment) {
        mongoTemplate.remove(entity)
    }
}
