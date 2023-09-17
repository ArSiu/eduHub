package com.arsiu.eduhub.repository.custom.impl

import com.arsiu.eduhub.model.Course
import com.arsiu.eduhub.repository.custom.ChapterCustomRepository
import com.arsiu.eduhub.repository.custom.CourseCustomRepository
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.aggregation.Aggregation.match
import org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation
import org.springframework.data.mongodb.core.aggregation.Aggregation.project
import org.springframework.data.mongodb.core.aggregation.Aggregation.sort
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Repository

@Repository
class CourseCustomRepositoryImpl(
    private val mongoTemplate: MongoTemplate,
    @Qualifier("chapterCustomRepositoryImpl")
    private val chapterCustomRepository: ChapterCustomRepository
) : CourseCustomRepository {

    override fun createCascade(entity: Course): Course {
        resetField(entity,"id")
        val course = mongoTemplate.save(entity)
        entity.chapters.forEach {
            it.course = course
            chapterCustomRepository.createCascade(it)
        }
        return mongoTemplate.save(course)
    }

    override fun updateCascade(entity: Course): Course {
        val query = Query.query(Criteria.where("id").`is`(entity.id))

        val existingEntity = mongoTemplate.findOne(query, Course::class.java)

        if(existingEntity != null) {
            if (existingEntity != entity) {

                existingEntity.chapters.filter { f ->
                    entity.chapters.none { s -> f.id == s.id }
                }.forEach { chapterCustomRepository.deleteCascade(it) }

                entity.chapters.forEach {
                    it.course = entity
                    chapterCustomRepository.updateCascade(it)
                }

                val update = Update()
                    .set("id", entity.id)
                    .set("name", entity.name)
                    .set("owner", entity.owner)
                    .set("chapters", entity.chapters)
                mongoTemplate.upsert(query, update, Course::class.java)
            }
            return entity
        }

        return createCascade(entity)
    }

    override fun deleteCascade(entity: Course) {
        entity.chapters.forEach {
            chapterCustomRepository.deleteCascade(it)
        }
        mongoTemplate.remove(entity)
    }

    override fun sortCoursesByInners(): List<Course> {
        val aggregation = newAggregation(
            match(Criteria.where("_class").`is`("com.arsiu.eduhub.model.Course")),
            project()
                .and("name").`as`("name")
                .and("chapters").size().`as`("chapterCount")
                .and("chapters.lessons").size().`as`("lessonCount"),
            sort(Sort.by(Sort.Order.desc("chapterCount"), Sort.Order.desc("lessonCount")))
        )

        return mongoTemplate.aggregate(aggregation, "course", Course::class.java).toList()
    }

}
