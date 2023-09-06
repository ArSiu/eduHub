package com.arsiu.eduhub.repository.custom.impl

import com.arsiu.eduhub.model.Lesson
import com.arsiu.eduhub.repository.custom.AssignmentCustomRepository
import com.arsiu.eduhub.repository.custom.LessonCustomRepository
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Repository

@Repository
class LessonCustomRepositoryImpl(
    private val mongoTemplate: MongoTemplate,
    @Qualifier("assignmentCustomRepositoryImpl")
    private val assignmentCustomRepository: AssignmentCustomRepository
) : LessonCustomRepository {

    override fun createCascade(entity: Lesson): Lesson {
        resetField(entity,"id")
        val lesson = mongoTemplate.save(entity)
        lesson.assignments.forEach {
            it.lesson = lesson
            assignmentCustomRepository.createCascade(it)
        }
        return mongoTemplate.save(lesson)
    }

    override fun updateCascade(entity: Lesson): Lesson {
        val query = Query.query(Criteria.where("id").`is`(entity.id))

        val existingEntity = mongoTemplate.findOne(query, Lesson::class.java)

        if (existingEntity != null) {
            if (existingEntity != entity) {
                existingEntity.assignments.filter { f ->
                    entity.assignments.none { s -> f.id == s.id }
                }.forEach { assignmentCustomRepository.deleteCascade(it) }

                entity.assignments.forEach {
                    it.lesson = entity
                    assignmentCustomRepository.updateCascade(it)
                }

                val update = Update()
                    .set("id", entity.id)
                    .set("name", entity.name)
                    .set("assignments", entity.assignments)
                mongoTemplate.upsert(query, update, Lesson::class.java)
            }
            return entity
        } else {
            return createCascade(entity)
        }
    }

    override fun deleteCascade(entity: Lesson) {
        entity.assignments.forEach {
            assignmentCustomRepository.deleteCascade(it)
        }
        mongoTemplate.remove(entity)
    }

}
