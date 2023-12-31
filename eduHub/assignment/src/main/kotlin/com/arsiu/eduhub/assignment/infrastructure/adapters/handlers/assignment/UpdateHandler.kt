package com.arsiu.eduhub.assignment.infrastructure.adapters.handlers.assignment

import com.arsiu.eduhub.assignment.application.port.AssignmentService
import com.arsiu.eduhub.assignment.domain.Assignment
import com.arsiu.eduhub.assignment.infrastructure.mapper.AssignmentProtoMapper
import com.arsiu.eduhub.v2.assignmentsvc.commonmodels.assignment.AssignmentRequest
import com.arsiu.eduhub.v2.assignmentsvc.input.reqreply.assignment.UpdateAssignmentRequest
import com.arsiu.eduhub.v2.assignmentsvc.output.reqreply.assignment.UpdateAssignmentResponse
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class UpdateHandler(
    private val service: AssignmentService,
    private val mapper: AssignmentProtoMapper
) : AssignmentHandler<UpdateAssignmentRequest, UpdateAssignmentResponse>() {

    fun handleUpdate(request: UpdateAssignmentRequest): Mono<UpdateAssignmentResponse> =
        when (request.request.requestCase) {
            AssignmentRequest.RequestCase.ASSIGNMENT -> update(request)
            AssignmentRequest.RequestCase.ASSIGNMENT_ID -> invalidIdResponse()
            AssignmentRequest.RequestCase.REQUEST_NOT_SET -> noRequestTypeSetResponse()
        }

    fun update(request: UpdateAssignmentRequest): Mono<UpdateAssignmentResponse> =
        service.update(mapper.toEntityUpdate(request.request.assignment))
            .map { assignment -> successUpdateResponse(assignment) }
            .doOnError { ex ->
                failureResponse(ex.javaClass.simpleName, ex.message ?: "Unknown error")
            }

    fun successUpdateResponse(assignment: Assignment): UpdateAssignmentResponse =
        UpdateAssignmentResponse.newBuilder().apply {
            responseBuilder.successBuilder
                .setMessage("Assignment updated successfully")
                .setAssignment(mapper.toResponseDto(assignment))
        }.build()

}
