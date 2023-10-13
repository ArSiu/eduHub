package com.arsiu.eduhub.chapter.domain

import com.arsiu.eduhub.lesson.domain.Lesson

data class Chapter(

    var id: String = "",

    var courseId: String = "",

    var lessons: MutableList<Lesson> = mutableListOf(),

    var name: String = ""

) {

    override fun toString(): String = " Chapter $name "

}
