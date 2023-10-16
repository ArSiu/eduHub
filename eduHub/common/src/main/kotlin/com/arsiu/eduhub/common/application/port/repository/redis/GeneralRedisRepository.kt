package com.arsiu.eduhub.common.application.port.repository.redis

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface GeneralRedisRepository<T, ID> {

    fun save(model: T): Mono<T>

    fun findById(id: ID): Mono<T>

    fun findAll(): Flux<T>

    fun deleteById(id: ID): Mono<Void>

}
