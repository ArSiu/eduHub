package com.arsiu.eduhub.assignment.infrastructure.persistence.repositories

import com.arsiu.eduhub.assignment.application.port.AssignmentMongoRepository
import com.arsiu.eduhub.assignment.domain.Assignment
import com.arsiu.eduhub.assignment.infrastructure.mapper.AssignmentToEntityMapper
import com.arsiu.eduhub.assignment.infrastructure.persistence.entity.MongoAssignment
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
class AssignmentMongoRepositoryImpl(
    private val reactiveMongoTemplate: ReactiveMongoTemplate,
    private val mapper: AssignmentToEntityMapper
) : AssignmentMongoRepository {

    override fun save(model: Assignment): Mono<Assignment> =
        reactiveMongoTemplate.save(mapper.toEntity(model))
            .map { mapper.toModel(it) }

    override fun findAll(): Flux<Assignment> =
        reactiveMongoTemplate.findAll(MongoAssignment::class.java).map {
            mapper.toModel(it)
        }

    override fun findById(id: String): Mono<Assignment> =
        reactiveMongoTemplate.findById(id, MongoAssignment::class.java).map {
            mapper.toModel(it)
        }

    override fun upsert(model: Assignment): Mono<Assignment> {

        val entity = mapper.toEntityWithId(model)
        val query = Query.query(Criteria.where("id").`is`(entity.id))

        val update = Update()
            .set("id", entity.id)
            .set("name", entity.name)

        return reactiveMongoTemplate.upsert(query, update, MongoAssignment::class.java)
            .thenReturn(model)
    }

    override fun remove(model: Assignment): Mono<Void> =
        reactiveMongoTemplate.remove(mapper.toEntityWithId(model)).then()

    override fun find(query: Query): Flux<Assignment> =
        reactiveMongoTemplate.find(query, MongoAssignment::class.java).map {
            mapper.toModel(it)
        }

}
