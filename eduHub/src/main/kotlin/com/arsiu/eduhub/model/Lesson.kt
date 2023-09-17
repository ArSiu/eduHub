package com.arsiu.eduhub.model

import lombok.AllArgsConstructor
import lombok.EqualsAndHashCode
import lombok.Getter
import lombok.NoArgsConstructor
import lombok.Setter
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.DocumentReference

@Document("lesson")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
class Lesson {

    @Id
    lateinit var id: String

    @DocumentReference
    lateinit var chapter: Chapter

    @DocumentReference
    lateinit var assignments: MutableList<Assignment>

    var name: String = ""

    override fun toString(): String = " Lesson \"$name\""

}
