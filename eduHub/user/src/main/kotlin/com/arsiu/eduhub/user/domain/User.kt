package com.arsiu.eduhub.user.domain

import com.arsiu.eduhub.course.domain.Course
import com.arsiu.eduhub.user.domain.Role.STUDENT

data class User(

    var id: String = "",

    var boughtCourses: MutableSet<Course> = mutableSetOf(),

    var createdCourses: MutableList<Course> = mutableListOf(),

    var firstName: String = "",

    var secondName: String = "",

    var email: String = "",

    var password: String = "",

    var role: Role = STUDENT

) {

    override fun toString(): String = " User $firstName $secondName "

}

enum class Role {
    ADMIN,
    MODERATOR,
    TEACHER,
    STUDENT
}
