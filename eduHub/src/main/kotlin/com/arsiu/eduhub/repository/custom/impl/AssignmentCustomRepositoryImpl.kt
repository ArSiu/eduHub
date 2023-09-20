package com.arsiu.eduhub.repository.custom.impl

import com.arsiu.eduhub.model.Assignment
import com.arsiu.eduhub.repository.custom.AssignmentCustomRepository
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Repository

@Repository
class AssignmentCustomRepositoryImpl(private val mongoTemplate: MongoTemplate) : AssignmentCustomRepository {

    override fun createCascade(entity: Assignment): Assignment {
        resetField(entity, "id")
        return mongoTemplate.save(entity)
    }

    override fun updateCascade(entity: Assignment): Assignment {
        val query = Query.query(Criteria.where("id").`is`(entity.id))

        val existingEntity = mongoTemplate.findOne(query, Assignment::class.java)

        if (existingEntity != null) {

            if (existingEntity != entity) {
                val update = Update()
                    .set("id", entity.id)
                    .set("name", entity.name)
                mongoTemplate.upsert(query, update, Assignment::class.java)
            }
            return entity
        }
        return createCascade(entity)
    }

    override fun deleteCascade(entity: Assignment) {
        mongoTemplate.remove(entity)
    }

}
