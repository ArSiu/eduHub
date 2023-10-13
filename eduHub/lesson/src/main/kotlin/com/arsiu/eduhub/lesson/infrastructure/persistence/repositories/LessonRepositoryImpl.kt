package com.arsiu.eduhub.lesson.infrastructure.persistence.repositories

import com.arsiu.eduhub.lesson.application.port.LessonRepository
import com.arsiu.eduhub.lesson.domain.Lesson
import com.arsiu.eduhub.lesson.infrastructure.mapper.LessonToEntityMapper
import com.arsiu.eduhub.lesson.infrastructure.persistence.entity.MongoLesson
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
class LessonRepositoryImpl(
    private val reactiveMongoTemplate: ReactiveMongoTemplate,
    private val mapper: LessonToEntityMapper
) : LessonRepository {

    override fun save(model: Lesson): Mono<Lesson> =
        reactiveMongoTemplate.save(mapper.toEntity(model)).map {
            mapper.toModelWithAssignments(it)
        }

    override fun findAll(): Flux<Lesson> =
        reactiveMongoTemplate.findAll(MongoLesson::class.java).map {
            mapper.toModel(it)
        }

    override fun findById(id: String): Mono<Lesson> =
        reactiveMongoTemplate.findById(id, MongoLesson::class.java).map {
            mapper.toModel(it)
        }

    override fun upsert(model: Lesson): Mono<Lesson> {
        val entity = mapper.toEntityWithId(model)
        val query = Query.query(Criteria.where("id").`is`(entity.id))

        val update = Update()
            .set("id", entity.id)
            .set("name", entity.name)
            .set("assignments", entity.assignments)

        return reactiveMongoTemplate.upsert(query, update, MongoLesson::class.java)
            .thenReturn(model)
    }

    override fun remove(model: Lesson): Mono<Void> =
        reactiveMongoTemplate.remove(mapper.toEntityWithId(model)).then()

    override fun find(query: Query): Flux<Lesson> =
        reactiveMongoTemplate.find(query, MongoLesson::class.java).map {
            mapper.toModel(it)
        }

}
