package com.arsiu.eduhub.lesson.domain

import com.arsiu.eduhub.assignment.domain.Assignment

data class Lesson(

    var id: String = "",

    var chapterId: String = "",

    var assignments: MutableList<Assignment> = mutableListOf(),

    var name: String = ""

) {

    override fun toString(): String = " Lesson \"$name\""

}
