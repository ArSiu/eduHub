package com.arsiu.eduhub.common.application.port.service

import com.arsiu.eduhub.common.application.port.repository.mongo.GeneralMongoRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.lang.reflect.Field

interface GeneralService<T, ID> {

    val logger: Logger
        get() = LoggerFactory.getLogger(GeneralMongoRepository::class.java)

    fun create(entity: T): Mono<T>

    fun findAll(): Flux<T>

    fun findById(id: ID): Mono<T>

    fun update(entity: T): Mono<T>

    fun delete(id: ID): Mono<Void>

    fun resetField(target: Any, fieldName: String) {
        try {
            val field: Field = target.javaClass.getDeclaredField(fieldName)
            field.isAccessible = true
            field.set(target, null)
        } catch (e: ReflectiveOperationException) {
            logger.error("An error occurred while resetting the field: {}", fieldName, e)
        }
    }

}
