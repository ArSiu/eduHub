package com.arsiu.eduhub.model

import lombok.AllArgsConstructor
import lombok.EqualsAndHashCode
import lombok.Getter
import lombok.NoArgsConstructor
import lombok.Setter
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import org.springframework.data.mongodb.core.mapping.FieldType.OBJECT_ID

@Document("assignment")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
class Assignment {

    @Id
    lateinit var id: String

    @Field(targetType = OBJECT_ID)
    lateinit var lessonId: String

    var name: String = ""

    override fun toString(): String = " Assignment \"$name\""

}
