package com.arsiu.eduhub.assignment.application.ports

import com.arsiu.eduhub.assignment.domain.Assignment
import com.arsiu.eduhub.common.application.ports.service.GeneralService
import reactor.core.publisher.Flux

interface AssignmentService : GeneralService<Assignment, String> {
    fun findAssignmentsForLesson(lessonId: String): Flux<Assignment>
}
