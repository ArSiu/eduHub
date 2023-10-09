package com.arsiu.eduhub.course.application.ports

import com.arsiu.eduhub.common.application.ports.repository.GeneralRepository
import com.arsiu.eduhub.course.domain.Course
import org.springframework.data.mongodb.core.aggregation.Aggregation
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux

@Repository
interface CourseRepository : GeneralRepository<Course, String> {
    fun aggregate(aggregation: Aggregation): Flux<Course>
}
