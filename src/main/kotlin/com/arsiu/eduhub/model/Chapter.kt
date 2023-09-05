package com.arsiu.eduhub.model

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.DocumentReference

@Document("chapter")
data class Chapter(

    @Id
    var id: String = ObjectId().toString(),

    var name: String = "",

    @DocumentReference
    var course: Course = Course()

) {

    @DocumentReference
    var lessons: MutableList<Lesson> = mutableListOf()
    override fun toString(): String {
        return " Chapter $name from $course "
    }


}
