package com.arsiu.eduhub.model

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.DocumentReference

@Document("lesson")
data class Lesson(

    @Id
    var id: String = ObjectId().toString(),

    var name: String = "",

    @DocumentReference
    var chapter: Chapter = Chapter()

) {

    @DocumentReference
    var assignments: MutableList<Assignment> = mutableListOf()


    override fun toString(): String {
        return " Lesson \"$name\" from chapter $chapter)"
    }


}
