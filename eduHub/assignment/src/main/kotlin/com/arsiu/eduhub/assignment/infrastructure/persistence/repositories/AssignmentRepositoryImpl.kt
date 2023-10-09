package com.arsiu.eduhub.assignment.infrastructure.persistence.repositories

import com.arsiu.eduhub.assignment.application.mapper.AssignmentToEntityMapper
import com.arsiu.eduhub.assignment.application.ports.AssignmentRepository
import com.arsiu.eduhub.assignment.domain.Assignment
import com.arsiu.eduhub.assignment.infrastructure.persistence.entity.AssignmentEntity
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
class AssignmentRepositoryImpl(
    private val reactiveMongoTemplate: ReactiveMongoTemplate,
    private val mapper: AssignmentToEntityMapper
) : AssignmentRepository {

    override fun save(model: Assignment): Mono<Assignment> =
        reactiveMongoTemplate.save(mapper.toEntity(model))
            .map { mapper.toModel(it) }

    override fun findAll(): Flux<Assignment> =
        reactiveMongoTemplate.findAll(AssignmentEntity::class.java).map {
            mapper.toModel(it)
        }

    override fun findById(id: String): Mono<Assignment> =
        reactiveMongoTemplate.findById(id, AssignmentEntity::class.java).map {
            mapper.toModel(it)
        }

    override fun upsert(model: Assignment): Mono<Assignment> {

        val entity = mapper.toEntityWithId(model)
        val query = Query.query(Criteria.where("id").`is`(entity.id))

        val update = Update()
            .set("id", entity.id)
            .set("name", entity.name)

        return reactiveMongoTemplate.upsert(query, update, AssignmentEntity::class.java)
            .thenReturn(model)
    }

    override fun remove(model: Assignment): Mono<Void> =
        reactiveMongoTemplate.remove(mapper.toEntityWithId(model)).then()

    override fun find(query: Query): Flux<Assignment> =
        reactiveMongoTemplate.find(query, AssignmentEntity::class.java).map {
            mapper.toModel(it)
        }

}
