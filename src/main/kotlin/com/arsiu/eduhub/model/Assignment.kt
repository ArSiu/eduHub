package com.arsiu.eduhub.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.DocumentReference

@Document("assignment")
data class Assignment(var name: String = "") {

    @Id
    lateinit var id: String

    @DocumentReference
    lateinit var lesson: Lesson

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Assignment) return false
        if (name != other.name) return false
        if ((this::id.isInitialized && other::id.isInitialized && id != other.id)
            || this::id.isInitialized != other::id.isInitialized
        ) return false
        if ((this::lesson.isInitialized && other::lesson.isInitialized && lesson != other.lesson)
            || this::lesson.isInitialized != other::lesson.isInitialized
        ) return false
        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        if (this::id.isInitialized) {
            result = 31 * result + id.hashCode()
        }
        if (this::lesson.isInitialized) {
            result = 31 * result + lesson.hashCode()
        }
        return result
    }

    override fun toString(): String = " Assignment \"$name\""

}
