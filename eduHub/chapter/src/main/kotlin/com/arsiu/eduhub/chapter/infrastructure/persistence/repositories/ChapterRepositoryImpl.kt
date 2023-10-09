package com.arsiu.eduhub.chapter.infrastructure.persistence.repositories

import com.arsiu.eduhub.chapter.application.mapper.ChapterToEntityMapper
import com.arsiu.eduhub.chapter.application.ports.ChapterRepository
import com.arsiu.eduhub.chapter.domain.Chapter
import com.arsiu.eduhub.chapter.infrastructure.persistence.entity.ChapterEntity
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
class ChapterRepositoryImpl(
    private val reactiveMongoTemplate: ReactiveMongoTemplate,
    private val mapper: ChapterToEntityMapper
) : ChapterRepository {

    override fun save(model: Chapter): Mono<Chapter> =
        reactiveMongoTemplate.save(mapper.toEntity(model)).map {
            mapper.toModelWithLessons(it)
        }

    override fun findAll(): Flux<Chapter> =
        reactiveMongoTemplate.findAll(ChapterEntity::class.java).map {
            mapper.toModel(it)
        }

    override fun findById(id: String): Mono<Chapter> =
        reactiveMongoTemplate.findById(id, ChapterEntity::class.java).map {
            mapper.toModel(it)
        }

    override fun upsert(model: Chapter): Mono<Chapter> {
        val entity = mapper.toEntityWithId(model)
        val query = Query.query(Criteria.where("id").`is`(entity.id))

        val update = Update()
            .set("id", entity.id)
            .set("name", entity.name)
            .set("lessons", entity.lessons)

        return reactiveMongoTemplate.upsert(query, update, ChapterEntity::class.java)
            .thenReturn(model)
    }

    override fun remove(model: Chapter): Mono<Void> =
        reactiveMongoTemplate.remove(mapper.toEntityWithId(model)).then()

    override fun find(query: Query): Flux<Chapter> =
        reactiveMongoTemplate.find(query, ChapterEntity::class.java).map {
            mapper.toModel(it)
        }

}
