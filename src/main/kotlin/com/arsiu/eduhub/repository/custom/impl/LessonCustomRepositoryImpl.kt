package com.arsiu.eduhub.repository.custom.impl

import com.arsiu.eduhub.model.Lesson
import com.arsiu.eduhub.repository.custom.AssignmentCustomRepository
import com.arsiu.eduhub.repository.custom.LessonCustomRepository
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.stereotype.Repository

@Repository
class LessonCustomRepositoryImpl(
    private val mongoTemplate: MongoTemplate,
    @Qualifier("assignmentCustomRepositoryImpl") private val assignmentCustomRepository: AssignmentCustomRepository
) : LessonCustomRepository {

    override fun createCascade(entity: Lesson): Lesson {
        val lesson = mongoTemplate.save(entity)
        lesson.assignments.forEach {
            it.lesson = lesson
            assignmentCustomRepository.createCascade(it)
        }
        return lesson
    }

    override fun deleteCascade(entity: Lesson) {
        entity.assignments.forEach {
            assignmentCustomRepository.deleteCascade(it)
        }
        mongoTemplate.remove(entity)
    }
}
