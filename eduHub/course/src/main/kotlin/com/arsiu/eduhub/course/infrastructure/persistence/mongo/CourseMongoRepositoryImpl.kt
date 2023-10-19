package com.arsiu.eduhub.course.infrastructure.persistence.mongo

import com.arsiu.eduhub.course.application.port.CoursePersistenceRepository
import com.arsiu.eduhub.course.domain.Course
import com.arsiu.eduhub.course.infrastructure.mapper.CourseToEntityMapper
import com.arsiu.eduhub.course.infrastructure.persistence.entity.MongoCourse
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.aggregation.Aggregation
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
class CourseMongoRepositoryImpl(
    private val reactiveMongoTemplate: ReactiveMongoTemplate,
    private val mapper: CourseToEntityMapper
) : CoursePersistenceRepository {

    override fun save(model: Course): Mono<Course> =
        reactiveMongoTemplate.save(mapper.toEntity(model)).map {
            mapper.toModelWithChapters(it)
        }

    override fun findAll(): Flux<Course> =
        reactiveMongoTemplate.findAll(MongoCourse::class.java).map {
            mapper.toModel(it)
        }

    override fun findById(id: String): Mono<Course> =
        reactiveMongoTemplate.findById(id, MongoCourse::class.java).map {
            mapper.toModel(it)
        }

    override fun upsert(model: Course): Mono<Course> {
        val entity = mapper.toEntityWithId(model)
        val query = Query.query(Criteria.where("id").`is`(entity.id))

        val update = Update()
            .set("id", entity.id)
            .set("name", entity.name)
            .set("ownerId", entity.ownerId)
            .set("chapters", entity.chapters)

        return reactiveMongoTemplate.upsert(query, update, MongoCourse::class.java)
            .thenReturn(model)
    }

    override fun remove(model: Course): Mono<Void> =
        reactiveMongoTemplate.remove(mapper.toEntityWithId(model)).then()

    override fun find(query: Query): Flux<Course> =
        reactiveMongoTemplate.find(query, MongoCourse::class.java).map {
            mapper.toModel(it)
        }

    override fun aggregate(aggregation: Aggregation): Flux<Course> =
        reactiveMongoTemplate.aggregate(
            aggregation,
            "courseEntity",
            MongoCourse::class.java
        ).map {
            mapper.toModel(it)
        }


}
