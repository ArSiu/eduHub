package com.arsiu.eduhub.repository.custom.impl

import com.arsiu.eduhub.model.Chapter
import com.arsiu.eduhub.repository.custom.ChapterCustomRepository
import com.arsiu.eduhub.repository.custom.LessonCustomRepository
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Repository

@Repository
class ChapterCustomRepositoryImpl(
    private val mongoTemplate: MongoTemplate,
    @Qualifier("lessonCustomRepositoryImpl")
    private val lessonCustomRepository: LessonCustomRepository
) : ChapterCustomRepository {

    override fun createCascade(entity: Chapter): Chapter {
        resetField(entity, "id")
        val chapter = mongoTemplate.save(entity)
        chapter.lessons.forEach {
            it.chapter = chapter
            lessonCustomRepository.createCascade(it)
        }
        return mongoTemplate.save(chapter)
    }

    override fun updateCascade(entity: Chapter): Chapter {
        val query = Query.query(Criteria.where("id").`is`(entity.id))

        val existingEntity = mongoTemplate.findOne(query, Chapter::class.java)

        if (existingEntity != null) {
            if (existingEntity != entity) {

                existingEntity.lessons.filter { f ->
                    entity.lessons.none { s -> f.id == s.id }
                }.forEach { lessonCustomRepository.deleteCascade(it) }

                entity.lessons.forEach {
                    it.chapter = entity
                    lessonCustomRepository.updateCascade(it)
                }

                val update = Update()
                    .set("id", entity.id)
                    .set("name", entity.name)
                    .set("lessons", entity.lessons)
                mongoTemplate.upsert(query, update, Chapter::class.java)
            }
            return entity
        }
        return createCascade(entity)
    }

    override fun deleteCascade(entity: Chapter) {
        entity.lessons.forEach {
            lessonCustomRepository.deleteCascade(it)
        }
        mongoTemplate.remove(entity)
    }

}
