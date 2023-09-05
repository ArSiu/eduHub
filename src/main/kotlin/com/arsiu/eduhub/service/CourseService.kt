package com.arsiu.eduhub.service

import com.arsiu.eduhub.annotation.NotifyTrigger
import com.arsiu.eduhub.exception.NotFoundException
import com.arsiu.eduhub.model.Course
import com.arsiu.eduhub.repository.CourseRepository
import com.arsiu.eduhub.service.interfaces.CourseServiceInterface
import com.arsiu.eduhub.service.interfaces.UserServiceInterface
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service

@Service
class CourseService @Autowired constructor(
    private val courseRepository: CourseRepository,
    @Lazy private val userService: UserServiceInterface
) : CourseServiceInterface {

    override fun findAll(): List<Course> = courseRepository.findAll().toList()

    override fun findById(id: String): Course =
        courseRepository.findById(id).orElseThrow { NotFoundException("Course with ID $id not found") }

    @NotifyTrigger("New Course Available ")
    override fun create(entity: Course): Course {
        entity.owner = userService.findById(entity.owner.id)
        return courseRepository.createCascade(entity)
    }

    override fun update(id: String, entity: Course): Course {
        entity.id = findById(id).id
        delete(id)
        return create(entity)
    }

    override fun delete(id: String) = courseRepository.deleteCascade(findById(id))

    override fun sortCoursesByInners() = courseRepository.sortCoursesByInners()

}
