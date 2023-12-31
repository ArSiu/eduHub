package com.arsiu.eduhub.user.infrastructure.persistence.mongo

import com.arsiu.eduhub.user.application.port.UserPersistenceRepository
import com.arsiu.eduhub.user.domain.User
import com.arsiu.eduhub.user.infrastructure.mapper.UserToEntityMapper
import com.arsiu.eduhub.user.infrastructure.persistence.entity.MongoUser
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
class UserMongoRepositoryImpl(
    private val reactiveMongoTemplate: ReactiveMongoTemplate,
    private val mapper: UserToEntityMapper
) : UserPersistenceRepository {

    override fun save(model: User): Mono<User> =
        reactiveMongoTemplate.save(mapper.toEntity(model))
            .map { mapper.toModel(it) }

    override fun findAll(): Flux<User> =
        reactiveMongoTemplate.findAll(MongoUser::class.java).map {
            mapper.toModel(it)
        }

    override fun findById(id: String): Mono<User> =
        reactiveMongoTemplate.findById(id, MongoUser::class.java).map {
            mapper.toModel(it)
        }

    override fun upsert(model: User): Mono<User> {
        val entity = mapper.toEntityWithId(model)

        val query = Query.query(Criteria.where("id").`is`(entity.id))

        val update = Update()
            .set("id", entity.id)
            .set("firstName", entity.firstName)
            .set("secondName", entity.secondName)
            .set("email", entity.email)
            .set("password", entity.password)
            .set("role", entity.role)

        return reactiveMongoTemplate.upsert(query, update, MongoUser::class.java)
            .thenReturn(model)
    }

    override fun remove(model: User): Mono<Void> =
        reactiveMongoTemplate.remove(mapper.toEntityWithId(model)).then()

    override fun find(query: Query): Flux<User> =
        reactiveMongoTemplate.find(query, MongoUser::class.java).map {
            mapper.toModel(it)
        }

}
