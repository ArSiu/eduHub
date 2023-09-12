package com.arsiu.eduhub.repository.custom

import java.lang.reflect.Field

interface CascadeRepository<T, ID> {

    fun createCascade(entity: T): T

    fun updateCascade(entity: T): T

    fun deleteCascade(entity: T)

    fun resetField(target: Any, fieldName: String) {
        try {
            val field: Field = target.javaClass.getDeclaredField(fieldName)
            field.isAccessible = true
            field.set(target, null)
        } catch (e: ReflectiveOperationException) {
            e.printStackTrace()
        }
    }

}
