package com.arsiu.eduhub.course.application.port

import com.arsiu.eduhub.common.application.port.GeneralPersistenceRepository
import com.arsiu.eduhub.course.domain.Course
import org.springframework.data.mongodb.core.aggregation.Aggregation
import reactor.core.publisher.Flux

interface CoursePersistenceRepository : GeneralPersistenceRepository<Course, String> {

    fun aggregate(aggregation: Aggregation): Flux<Course>

}
