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
import org.springframework.stereotype.Repository


@Repository
class CourseCustomRepositoryImpl(
    private val mongoTemplate: MongoTemplate,
    @Qualifier("chapterCustomRepositoryImpl") private val chapterCustomRepository: ChapterCustomRepository
) : CourseCustomRepository {

    override fun createCascade(entity: Course): Course {
        val course = mongoTemplate.save(entity)
        entity.chapters.forEach {
            it.course = course
            chapterCustomRepository.createCascade(it)
        }
        return course
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
