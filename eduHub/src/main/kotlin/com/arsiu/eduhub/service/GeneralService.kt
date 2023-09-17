package com.arsiu.eduhub.service

interface GeneralService<T, ID> {

    fun findAll(): List<T>

    fun findById(id: ID): T

    fun create(entity: T): T

    fun update(entity: T): T

    fun delete(id: ID)

}
