package com.arsiu.eduhub.model

import com.arsiu.eduhub.model.enums.Role
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.DocumentReference

@Document("user")
data class User(

    @Id
    var id: String = ObjectId().toString(),

    var firstName: String = "",

    var secondName: String = "",

    var email: String = "",

    var password: String = "",

    var role: Role = Role.STUDENT,

    ) {

    @DocumentReference
    var boughtCourses: MutableSet<Course> = mutableSetOf()

    @DocumentReference
    var createdCourses: MutableList<Course> = mutableListOf()


    override fun toString(): String = " User $firstName $secondName "
}
