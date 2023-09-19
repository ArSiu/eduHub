package com.arsiu.eduhub.service.impl

import com.arsiu.eduhub.annotation.NotifyTrigger
import com.arsiu.eduhub.exception.NotFoundException
import com.arsiu.eduhub.model.Course
import com.arsiu.eduhub.repository.CourseRepository
import com.arsiu.eduhub.service.CourseService
import com.arsiu.eduhub.service.UserService
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service

@Service
class CourseServiceImpl(
    private val courseRepository: CourseRepository,
    @Lazy
    private val userService: UserService
) : CourseService {

    override fun findAll(): List<Course> =
        courseRepository.findAll().toList()

    override fun findById(id: String): Course =
        courseRepository.findById(id).orElseThrow { NotFoundException("Course with ID $id not found") }

    @NotifyTrigger("New Course Available ")
    override fun create(entity: Course): Course {
        entity.owner.id.let { entity.owner = userService.findById(it) }
        return courseRepository.createCascade(entity)
    }

    override fun update(entity: Course): Course {
        findById(entity.id)
        return courseRepository.updateCascade(entity)
    }

    override fun delete(id: String) =
        courseRepository.deleteCascade(findById(id))

    override fun sortCoursesByInners(): List<Course> =
        courseRepository.sortCoursesByInners()

}
