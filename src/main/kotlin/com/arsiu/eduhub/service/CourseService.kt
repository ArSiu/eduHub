package com.arsiu.eduhub.service

import com.arsiu.eduhub.exception.NotFoundException
import com.arsiu.eduhub.model.Course
import com.arsiu.eduhub.repository.CourseRepository
import com.arsiu.eduhub.service.interfaces.CourseServiceInterface
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service

@Service
class CourseService @Autowired constructor(
    val courseRepository: CourseRepository,
    @Lazy val chapterService: ChapterService,
    val userService: UserService,
) : CourseServiceInterface {

    override fun findAll(): List<Course> = courseRepository.findAll().toList()

    override fun findById(id: Long): Course =
        courseRepository.findById(id).orElseThrow { NotFoundException("Course with ID $id not found") }

    override fun create(entity: Course): Course {
        entity.owner = userService.findById(entity.owner.id)

        val chapters = entity.chapters.toList()

        entity.chapters.clear()

        val createdCourse = courseRepository.save(entity)

        val updatedChapters = chapters.map { chapter ->
            chapter.course = createdCourse
            chapterService.create(chapter)
        }

        createdCourse.chapters.addAll(updatedChapters)

        return courseRepository.save(createdCourse)
    }

    override fun update(id: Long, entity: Course) {
        userService.findById(entity.owner.id)
        entity.id = findById(id).id
        create(entity)
    }

    override fun delete(id: Long) = courseRepository.deleteById(id)

}
