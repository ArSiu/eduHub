package com.arsiu.eduhub.service

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface GeneralService<T, ID> {

    fun findAll(): Flux<T>

    fun findById(id: ID): Mono<T>

    fun create(entity: T): Mono<T>

    fun update(entity: T): Mono<T>

    fun delete(id: ID): Mono<Void>

}
