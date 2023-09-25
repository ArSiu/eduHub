package com.arsiu.eduhub.repository

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.lang.reflect.Field

interface CascadeRepository<T, ID> {
    val logger: Logger
        get() = LoggerFactory.getLogger(CascadeRepository::class.java)

    fun createCascade(entity: T): Mono<T>

    fun findAllCascade(): Flux<T>

    fun findByIdCascade(id: ID): Mono<T>

    fun updateCascade(entity: T): Mono<T>

    fun deleteCascade(entity: T): Mono<Void>

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
