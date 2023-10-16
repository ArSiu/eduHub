package com.arsiu.eduhub.assignment.infrastructure.adapters.handlers.assignment

import com.arsiu.eduhub.assignment.application.port.AssignmentService
import com.arsiu.eduhub.assignment.domain.Assignment
import com.arsiu.eduhub.assignment.infrastructure.mapper.AssignmentProtoMapper
import com.arsiu.eduhub.v2.assignmentsvc.commonmodels.assignment.AssignmentRequest
import com.arsiu.eduhub.v2.assignmentsvc.input.reqreply.assignment.FindByIdAssignmentRequest
import com.arsiu.eduhub.v2.assignmentsvc.output.reqreply.assignment.FindByIdAssignmentResponse
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class FindByIdHandler(
    private val service: AssignmentService,
    private val mapper: AssignmentProtoMapper
) : AssignmentHandler<FindByIdAssignmentRequest, FindByIdAssignmentResponse>() {

    fun handleFindById(request: FindByIdAssignmentRequest): Mono<FindByIdAssignmentResponse> =
        when (request.request.requestCase) {
            AssignmentRequest.RequestCase.ASSIGNMENT_ID -> findById(request)
            AssignmentRequest.RequestCase.ASSIGNMENT -> unsupportedRequestTypeResponse()
            AssignmentRequest.RequestCase.REQUEST_NOT_SET -> noRequestTypeSetResponse()
        }

    fun findById(request: FindByIdAssignmentRequest): Mono<FindByIdAssignmentResponse> =
        service.findById(request.request.assignmentId.id)
            .map { assignment -> successFindByIdResponse(assignment) }
            .doOnError { ex ->
                failureResponse(ex.javaClass.simpleName, ex.message ?: "Unknown error")
            }

    fun successFindByIdResponse(assignment: Assignment): FindByIdAssignmentResponse =
        FindByIdAssignmentResponse.newBuilder().apply {
            responseBuilder.successBuilder
                .setMessage("Assignment found successfully")
                .setAssignment(mapper.toResponseDto(assignment))
        }.build()

}
