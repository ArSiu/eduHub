package com.arsiu.eduhub.course.infrastructure.persistence.entity

import com.arsiu.eduhub.chapter.domain.Chapter
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.DocumentReference
import org.springframework.data.mongodb.core.mapping.Field
import org.springframework.data.mongodb.core.mapping.FieldType.OBJECT_ID

@Document("course")
class MongoCourse {

    @Id
    lateinit var id: String

    @Field(targetType = OBJECT_ID)
    lateinit var ownerId: String

    @DocumentReference
    lateinit var chapters: MutableList<Chapter>

    @DocumentReference
    lateinit var students: MutableSet<Any>

    var name: String = ""

    override fun toString(): String = " Course \"$name\" by "

}
