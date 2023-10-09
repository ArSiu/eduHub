package com.arsiu.eduhub.assignment.domain

import lombok.AllArgsConstructor
import lombok.EqualsAndHashCode
import lombok.Getter
import lombok.NoArgsConstructor
import lombok.Setter

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
class Assignment {
    var id: String = ""

    var lessonId: String = ""

    var name: String = ""

    override fun toString(): String = " Assignment \"$name\""
}
