package com.arsiu.eduhub.assignment.application.port

import com.arsiu.eduhub.assignment.domain.Assignment
import com.arsiu.eduhub.common.application.port.GeneralService
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface AssignmentService : GeneralService<Assignment, String> {

    fun findAssignmentsForLesson(lessonId: String): Flux<Assignment>

    fun createAssignmentsForLesson(
        lessonId: String,
        assignments: MutableList<Assignment>
    ): Mono<Void>

    fun updateAssignmentsForLesson(
        lessonId: String,
        assignments: List<Assignment>
    ): Flux<Assignment>

    fun deleteRemovedAssignmentsForLesson(
        existingAssignments: List<Assignment>,
        newAssignments: List<Assignment>
    ): Mono<Void>

}
