package com.arsiu.eduhub.assignment.application.port

import com.arsiu.eduhub.assignment.domain.Assignment
import com.arsiu.eduhub.common.application.port.service.GeneralService
import reactor.core.publisher.Flux

interface AssignmentService : GeneralService<Assignment, String> {
    fun findAssignmentsForLesson(lessonId: String): Flux<Assignment>
}
