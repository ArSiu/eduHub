package com.arsiu.eduhub.lesson.infrastructure.persistence.entity

import com.arsiu.eduhub.assignment.domain.Assignment
import lombok.AllArgsConstructor
import lombok.EqualsAndHashCode
import lombok.Getter
import lombok.NoArgsConstructor
import lombok.Setter
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.DocumentReference
import org.springframework.data.mongodb.core.mapping.Field
import org.springframework.data.mongodb.core.mapping.FieldType.OBJECT_ID

@Document("lessonEntity")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
class LessonEntity {

    @Id
    lateinit var id: String

    @Field(targetType = OBJECT_ID)
    lateinit var chapterId: String

    @DocumentReference
    lateinit var assignments: MutableList<Assignment>

    var name: String = ""

    override fun toString(): String = " Lesson \"$name\""

}
