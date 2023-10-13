package com.arsiu.eduhub.assignment.infrastructure.adapters.handlers.assignment

import com.arsiu.eduhub.assignment.infrastructure.mapper.AssignmentProtoMapper
import com.arsiu.eduhub.assignment.application.port.AssignmentService
import com.arsiu.eduhub.assignment.domain.Assignment
import com.arsiu.eduhub.v2.assignmentsvc.input.reqreply.assignment.FindAllAssignmentRequest
import com.arsiu.eduhub.v2.assignmentsvc.output.reqreply.assignment.FindAllAssignmentResponse
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class FindAllHandler(
    private val service: AssignmentService,
    private val mapper: AssignmentProtoMapper
) : AssignmentHandler<FindAllAssignmentRequest, FindAllAssignmentResponse>() {

    @Suppress("UnusedParameter")
    fun handleFindAll(request: FindAllAssignmentRequest): Mono<FindAllAssignmentResponse> =
        findAll()

    fun findAll(): Mono<FindAllAssignmentResponse> =
        service.findAll().collectList().map {
            successFindAllResponse(it)
        }.doOnError {
            failureResponse(it.javaClass.simpleName, it.message ?: "Unknown error")
        }

    fun successFindAllResponse(assignments: List<Assignment>): FindAllAssignmentResponse =
        FindAllAssignmentResponse.newBuilder().apply {
            responseBuilder.successBuilder
                .setMessage("Assignments returned successfully")
                .assignmentsBuilder
                .addAllAssignment(mapper.toResponseDtoList(assignments))
        }.build()
}
