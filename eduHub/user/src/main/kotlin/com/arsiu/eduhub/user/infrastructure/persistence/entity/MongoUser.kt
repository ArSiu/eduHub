package com.arsiu.eduhub.user.infrastructure.persistence.entity

import com.arsiu.eduhub.course.domain.Course
import com.arsiu.eduhub.user.domain.Role
import com.arsiu.eduhub.user.domain.Role.STUDENT
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.DocumentReference

@Document("user")
class MongoUser {

    @Id
    lateinit var id: String

    @DocumentReference
    lateinit var boughtCourses: MutableSet<Course>

    @DocumentReference
    lateinit var createdCourses: MutableList<Course>

    var firstName: String = ""

    var secondName: String = ""

    var email: String = ""

    var password: String = ""

    var role: Role = STUDENT

    override fun toString(): String = " User $firstName $secondName "

}
