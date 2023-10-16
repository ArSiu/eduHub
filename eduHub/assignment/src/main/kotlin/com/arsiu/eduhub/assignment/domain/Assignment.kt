package com.arsiu.eduhub.assignment.domain

data class Assignment(

    var id: String = "",

    var lessonId: String = "",

    var name: String = ""

) {

    override fun toString(): String = " Assignment \"$name\""

}
