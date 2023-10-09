package com.arsiu.eduhub.lesson.domain

import com.arsiu.eduhub.assignment.domain.Assignment
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
class Lesson {

    lateinit var id: String

    lateinit var chapterId: String

    lateinit var assignments: MutableList<Assignment>

    var name: String = ""

    override fun toString(): String = " Lesson \"$name\""

}
