package com.arsiu.eduhub.chapter.domain

import com.arsiu.eduhub.lesson.domain.Lesson
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
class Chapter {

    lateinit var id: String

    lateinit var courseId: String

    lateinit var lessons: MutableList<Lesson>

    var name: String = ""

    override fun toString(): String = " Chapter $name "

}
