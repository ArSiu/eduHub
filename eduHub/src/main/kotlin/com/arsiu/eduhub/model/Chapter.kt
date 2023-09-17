package com.arsiu.eduhub.model

import lombok.AllArgsConstructor
import lombok.EqualsAndHashCode
import lombok.Getter
import lombok.NoArgsConstructor
import lombok.Setter
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.DocumentReference

@Document("chapter")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
class Chapter {

    @Id
    lateinit var id: String

    @DocumentReference
    lateinit var course: Course

    @DocumentReference
    lateinit var lessons: MutableList<Lesson>

    var name: String = ""

    override fun toString(): String = " Chapter $name "

}
