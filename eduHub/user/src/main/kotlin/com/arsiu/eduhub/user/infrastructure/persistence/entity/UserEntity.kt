package com.arsiu.eduhub.user.infrastructure.persistence.entity

import com.arsiu.eduhub.course.domain.Course
import com.arsiu.eduhub.user.domain.Role
import com.arsiu.eduhub.user.domain.Role.STUDENT
import lombok.AllArgsConstructor
import lombok.EqualsAndHashCode
import lombok.Getter
import lombok.NoArgsConstructor
import lombok.Setter

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
class UserEntity {

    lateinit var id: String

    lateinit var boughtCourses: MutableSet<Course>

    lateinit var createdCourses: MutableList<Course>

    var firstName: String = ""

    var secondName: String = ""

    var email: String = ""

    var password: String = ""

    var role: Role = STUDENT

    override fun toString(): String = " User $firstName $secondName "

}
