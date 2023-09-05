package com.arsiu.eduhub.repository.custom

interface CascadeRepository<T, ID> {

    fun createCascade(entity: T): T

    fun deleteCascade(entity: T)
}
