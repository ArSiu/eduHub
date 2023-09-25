package com.arsiu.eduhub.service.impl

import com.arsiu.eduhub.annotation.NotifyTrigger
import com.arsiu.eduhub.exception.NotFoundException
import com.arsiu.eduhub.model.Course
import com.arsiu.eduhub.repository.CourseRepository
import com.arsiu.eduhub.service.CourseService
import com.arsiu.eduhub.service.UserService
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class CourseServiceImpl(
    private val courseRepository: CourseRepository,
    @Lazy
    private val userService: UserService
) : CourseService {

    override fun findAll(): Flux<Course> =
        courseRepository.findAllCascade()

    override fun findById(id: String): Mono<Course> =
        courseRepository.findByIdCascade(id)
            .switchIfEmpty(Mono.error(NotFoundException("Course with ID $id not found")))

    @NotifyTrigger("New Course Available ")
    override fun create(entity: Course): Mono<Course> =
        userService.findById(entity.ownerId).then(courseRepository.createCascade(entity))

    override fun update(entity: Course): Mono<Course> =
        findById(entity.id).then(courseRepository.updateCascade(entity))

    override fun delete(id: String): Mono<Void> =
        findById(id).flatMap(courseRepository::deleteCascade)

    override fun sortCoursesByInners(): Flux<Course> =
        courseRepository.sortCoursesByInners()

}
