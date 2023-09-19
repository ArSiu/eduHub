package com.arsiu.eduhub.repository.custom

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.lang.reflect.Field

interface CascadeRepository<T, ID> {
    val logger: Logger
        get() = LoggerFactory.getLogger(CascadeRepository::class.java)

    fun createCascade(entity: T): T

    fun updateCascade(entity: T): T

    fun deleteCascade(entity: T)

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
