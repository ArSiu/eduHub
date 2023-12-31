package com.arsiu.eduhub.chapter.infrastructure.persistence.entity

import com.arsiu.eduhub.lesson.domain.Lesson
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.DocumentReference
import org.springframework.data.mongodb.core.mapping.Field
import org.springframework.data.mongodb.core.mapping.FieldType.OBJECT_ID

@Document("chapter")
class MongoChapter {

    @Id
    lateinit var id: String

    @Field(targetType = OBJECT_ID)
    lateinit var courseId: String

    @DocumentReference
    lateinit var lessons: MutableList<Lesson>

    var name: String = ""

    override fun toString(): String = " Chapter $name "

}
