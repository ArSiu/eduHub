package com.arsiu.eduhub.model

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.DocumentReference

@Document("course")
data class Course(

    @Id
    var id: String = ObjectId().toString(),

    var name: String = "",

    @DocumentReference
    var owner: User = User()

) {

    @DocumentReference
    var chapters: MutableList<Chapter> = mutableListOf()

    @DocumentReference
    var students: MutableSet<User> = mutableSetOf()

    override fun toString(): String {
        return " Course \"$name\" by $owner "
    }

}
