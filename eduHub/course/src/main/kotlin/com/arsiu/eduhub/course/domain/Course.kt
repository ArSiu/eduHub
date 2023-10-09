package com.arsiu.eduhub.course.domain

import com.arsiu.eduhub.chapter.domain.Chapter
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
class Course {

    lateinit var id: String

    lateinit var ownerId: String

    lateinit var chapters: MutableList<Chapter>

    lateinit var students: MutableSet<Any>

    var name: String = ""

    override fun toString(): String = " Course \"$name\" by "

}
