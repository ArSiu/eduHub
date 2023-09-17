package com.arsiu.eduhub.model

import lombok.AllArgsConstructor
import lombok.EqualsAndHashCode
import lombok.Getter
import lombok.NoArgsConstructor
import lombok.Setter
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.DocumentReference

@Document("course")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
class Course {

    @Id
    lateinit var id: String

    @DocumentReference
    lateinit var owner: User

    @DocumentReference
    lateinit var chapters: MutableList<Chapter>

    @DocumentReference
    lateinit var students: MutableSet<User>

    var name: String = ""

    override fun toString(): String = " Course \"$name\" by $owner "

}
