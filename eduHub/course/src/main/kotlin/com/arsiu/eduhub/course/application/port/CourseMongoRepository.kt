package com.arsiu.eduhub.course.application.port

import com.arsiu.eduhub.common.application.port.repository.mongo.GeneralMongoRepository
import com.arsiu.eduhub.course.domain.Course
import org.springframework.data.mongodb.core.aggregation.Aggregation
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux

@Repository
interface CourseMongoRepository : GeneralMongoRepository<Course, String> {
    fun aggregate(aggregation: Aggregation): Flux<Course>
}
