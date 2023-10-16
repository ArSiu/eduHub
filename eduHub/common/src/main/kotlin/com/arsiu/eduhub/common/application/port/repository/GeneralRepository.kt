package com.arsiu.eduhub.common.application.port.repository

import org.springframework.data.mongodb.core.query.Query
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface GeneralRepository<T, ID> {

    fun save(model: T): Mono<T>

    fun findAll(): Flux<T>

    fun findById(id: ID): Mono<T>

    fun upsert(model: T): Mono<T>

    fun remove(model: T): Mono<Void>

    fun find(query: Query): Flux<T>

}
