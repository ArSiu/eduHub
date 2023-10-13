package com.arsiu.eduhub.assignment.infrastructure.persistence.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import org.springframework.data.mongodb.core.mapping.FieldType.OBJECT_ID

@Document("assignment")
class MongoAssignment {

    @Id
    lateinit var id: String

    @Field(targetType = OBJECT_ID)
    lateinit var lessonId: String

    var name: String = ""

    override fun toString(): String = " Assignment \"$name\""

}
