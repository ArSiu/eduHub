package com.arsiu.eduhub.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.DocumentReference

@Document("chapter")
data class Chapter(var name: String = "") {

    @Id
    lateinit var id: String

    @DocumentReference
    lateinit var course: Course

    @DocumentReference
    lateinit var lessons: MutableList<Lesson>

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Chapter) return false
        if (name != other.name) return false
        if ((this::id.isInitialized && other::id.isInitialized && id != other.id)
            || this::id.isInitialized != other::id.isInitialized
        ) return false
        if ((this::course.isInitialized && other::course.isInitialized && course != other.course)
            || this::course.isInitialized != other::course.isInitialized
        ) return false
        if ((this::lessons.isInitialized && other::lessons.isInitialized && lessons != other.lessons)
            || this::lessons.isInitialized != other::lessons.isInitialized
        ) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        if (this::id.isInitialized) {
            result = 31 * result + id.hashCode()
        }
        if (this::course.isInitialized) {
            result = 31 * result + course.hashCode()
        }
        if (this::lessons.isInitialized) {
            result = 31 * result + lessons.hashCode()
        }

        return result
    }

    override fun toString(): String = " Chapter $name "

}
