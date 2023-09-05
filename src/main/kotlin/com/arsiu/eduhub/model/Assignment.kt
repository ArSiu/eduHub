package com.arsiu.eduhub.model

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.DocumentReference

@Document("assignment")
data class Assignment(

    @Id
    var id: String = ObjectId().toString(),

    var name: String = ""

) {

    @DocumentReference
    var lesson: Lesson = Lesson()

    override fun toString(): String {
        return " Assignment \"$name\" from lesson $lesson)"
    }
}
