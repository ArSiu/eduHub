package com.arsiu.eduhub.it.base

import com.arsiu.eduhub.assignment.domain.Assignment
import com.arsiu.eduhub.chapter.domain.Chapter
import com.arsiu.eduhub.course.application.port.CourseRedisRepository
import com.arsiu.eduhub.course.domain.Course
import com.arsiu.eduhub.lesson.domain.Lesson
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
abstract class BaseCourseTest {

    @Autowired
    protected lateinit var courseRedisRepository: CourseRedisRepository

    protected final fun getTestCourse(): Course {
        val assignment = Assignment().apply {
            id = "6528ffd46949bd51bd1f6f1e"
            name = "string"
        }

        val lesson = Lesson().apply {
            id = "6528ffd46949bd51bd1f6f1d"
            name = "string"
            assignments = mutableListOf(assignment)
        }
        assignment.lessonId = lesson.id

        val chapter = Chapter().apply {
            id = "6528ffd46949bd51bd1f6f1c"
            name = "string"
            lessons = mutableListOf(lesson)
        }
        lesson.chapterId = chapter.id

        val course = Course().apply {
            id = "6528ffd46949bd51bd1f6f1b"
            name = "string"
            ownerId = "6527dbd89779554ca235263e"
            chapters = mutableListOf(chapter)
        }
        chapter.courseId = course.id

        return course
    }

}
