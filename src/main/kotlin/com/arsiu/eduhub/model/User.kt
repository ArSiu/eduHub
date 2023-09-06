package com.arsiu.eduhub.model

import com.arsiu.eduhub.model.enums.Role
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.DocumentReference

@Document("user")
data class User(

    var firstName: String = "",

    var secondName: String = "",

    var email: String = "",

    var password: String = "",

    var role: Role = Role.STUDENT,

) {

    @Id
    lateinit var id: String

    @DocumentReference
    lateinit var boughtCourses: MutableSet<Course>

    @DocumentReference
    lateinit var createdCourses: MutableList<Course>

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is User) return false

        if (firstName != other.firstName) return false
        if (secondName != other.secondName) return false
        if (email != other.email) return false
        if (password != other.password) return false
        if (role != other.role) return false
        if ((this::id.isInitialized && other::id.isInitialized && id != other.id)
            || this::id.isInitialized != other::id.isInitialized
        ) return false
        if ((this::boughtCourses.isInitialized && other::boughtCourses.isInitialized && boughtCourses != other.boughtCourses)
            || this::boughtCourses.isInitialized != other::boughtCourses.isInitialized
        ) return false
        if ((this::createdCourses.isInitialized && other::createdCourses.isInitialized && createdCourses != other.createdCourses)
            || this::createdCourses.isInitialized != other::createdCourses.isInitialized
        ) return false

        return true
    }

    override fun hashCode(): Int {
        var result = firstName.hashCode()
        result = 31 * result + secondName.hashCode()
        result = 31 * result + email.hashCode()
        result = 31 * result + password.hashCode()
        result = 31 * result + role.hashCode()
        if (this::id.isInitialized) {
            result = 31 * result + id.hashCode()
        }
        if (this::id.isInitialized) {
            result = 31 * result + boughtCourses.hashCode()
        }
        if (this::id.isInitialized) {
            result = 31 * result + createdCourses.hashCode()
        }
        return result
    }

    override fun toString(): String = " User $firstName $secondName "

}
