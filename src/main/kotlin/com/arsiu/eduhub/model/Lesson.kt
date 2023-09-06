package com.arsiu.eduhub.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.DocumentReference

@Document("lesson")
data class Lesson(var name: String = "") {

    @Id
    lateinit var id: String

    @DocumentReference
    lateinit var chapter: Chapter

    @DocumentReference
    lateinit var assignments: MutableList<Assignment>

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Lesson) return false

        if (name != other.name) return false
        if ((this::id.isInitialized && other::id.isInitialized && id != other.id)
            || this::id.isInitialized != other::id.isInitialized
        ) return false
        if ((this::chapter.isInitialized && other::chapter.isInitialized && chapter != other.chapter)
            || this::chapter.isInitialized != other::chapter.isInitialized
        ) return false
        if ((this::assignments.isInitialized && other::assignments.isInitialized && assignments != other.assignments)
            || this::assignments.isInitialized != other::assignments.isInitialized
        ) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        if (this::id.isInitialized) {
            result = 31 * result + id.hashCode()
        }
        if (this::chapter.isInitialized) {
            result = 31 * result + chapter.hashCode()
        }
        if (this::assignments.isInitialized) {
            result = 31 * result + assignments.hashCode()
        }
        return result
    }

    override fun toString(): String = " Lesson \"$name\""

}
