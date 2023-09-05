package com.arsiu.eduhub.repository.custom.impl

import com.arsiu.eduhub.model.Chapter
import com.arsiu.eduhub.repository.custom.ChapterCustomRepository
import com.arsiu.eduhub.repository.custom.LessonCustomRepository
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.stereotype.Repository

@Repository
class ChapterCustomRepositoryImpl(
    private val mongoTemplate: MongoTemplate,
    @Qualifier("lessonCustomRepositoryImpl") private val lessonCustomRepository: LessonCustomRepository
) : ChapterCustomRepository {

    override fun createCascade(entity: Chapter): Chapter {
        val chapter = mongoTemplate.save(entity)
        chapter.lessons.forEach {
            it.chapter = chapter
            lessonCustomRepository.createCascade(it)
        }
        return chapter
    }

    override fun deleteCascade(entity: Chapter) {
        entity.lessons.forEach {
            lessonCustomRepository.deleteCascade(it)
        }
        mongoTemplate.remove(entity)
    }
}
