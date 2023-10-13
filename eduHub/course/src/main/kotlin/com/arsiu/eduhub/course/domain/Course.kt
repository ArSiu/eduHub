package com.arsiu.eduhub.course.domain

import com.arsiu.eduhub.chapter.domain.Chapter

data class Course(

    var id: String = "",

    var ownerId: String = "",

    var chapters: MutableList<Chapter> = mutableListOf(),

    var students: MutableSet<Any> = mutableSetOf(),

    var name: String = ""

) {

    override fun toString(): String = " Course \"$name\" by "

}
