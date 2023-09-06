package com.arsiu.eduhub.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.DocumentReference

@Document("course")
data class Course(var name: String = "") {

    @Id
    lateinit var id: String

    @DocumentReference
    lateinit var owner: User

    @DocumentReference
    lateinit var chapters: MutableList<Chapter>

    @DocumentReference
    lateinit var students: MutableSet<User>

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Course) return false

        if (name != other.name) return false
        if ((this::id.isInitialized && other::id.isInitialized && id != other.id)
            || this::id.isInitialized != other::id.isInitialized
        ) return false
        if ((this::owner.isInitialized && other::owner.isInitialized && owner != other.owner)
            || this::owner.isInitialized != other::owner.isInitialized
        ) return false
        if ((this::chapters.isInitialized && other::chapters.isInitialized && chapters != other.chapters)
            || this::chapters.isInitialized != other::chapters.isInitialized
        ) return false
        if ((this::students.isInitialized && other::students.isInitialized && students != other.students)
            || this::students.isInitialized != other::students.isInitialized
        ) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        if (this::id.isInitialized) {
            result = 31 * result + id.hashCode()
        }
        if (this::owner.isInitialized) {
            result = 31 * result + owner.hashCode()
        }
        if (this::chapters.isInitialized) {
            result = 31 * result + chapters.hashCode()
        }
        if (this::students.isInitialized) {
            result = 31 * result + students.hashCode()
        }
        return result
    }

    override fun toString(): String = " Course \"$name\" by $owner "

}
