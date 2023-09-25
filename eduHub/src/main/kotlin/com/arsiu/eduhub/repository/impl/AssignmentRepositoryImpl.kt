package com.arsiu.eduhub.repository.impl

import com.arsiu.eduhub.model.Assignment
import com.arsiu.eduhub.repository.AssignmentRepository
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
class AssignmentRepositoryImpl(
    private val reactiveMongoTemplate: ReactiveMongoTemplate
) : AssignmentRepository {

    override fun createCascade(entity: Assignment): Mono<Assignment> {
        resetField(entity, "id")
        return reactiveMongoTemplate.save(entity)
    }

    override fun findByIdCascade(id: String): Mono<Assignment> =
        reactiveMongoTemplate.findById(id, Assignment::class.java)

    override fun findAllCascade(): Flux<Assignment> =
        reactiveMongoTemplate.findAll(Assignment::class.java)

    override fun updateCascade(entity: Assignment): Mono<Assignment> =
        findByIdCascade(entity.id)
            .flatMap { existingEntity ->
                if (existingEntity != entity) {
                    val query = Query.query(Criteria.where("id").`is`(entity.id))

                    val update = Update()
                        .set("id", entity.id)
                        .set("name", entity.name)

                    reactiveMongoTemplate.upsert(query, update, Assignment::class.java).thenReturn(entity)
                } else {
                    Mono.just(existingEntity)
                }
            }
            .switchIfEmpty(Mono.defer { createCascade(entity) })

    override fun deleteCascade(entity: Assignment): Mono<Void> =
        reactiveMongoTemplate.remove(entity).then()

}
