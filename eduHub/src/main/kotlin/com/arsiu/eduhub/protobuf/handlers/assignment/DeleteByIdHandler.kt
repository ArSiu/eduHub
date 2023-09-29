package com.arsiu.eduhub.protobuf.handlers.assignment

import com.arsiu.eduhub.service.AssignmentService
import com.arsiu.eduhub.v2.assignmentsvc.commonmodels.assignment.AssignmentRequest
import com.arsiu.eduhub.v2.assignmentsvc.input.reqreply.assignment.DeleteByIdAssignmentRequest
import com.arsiu.eduhub.v2.assignmentsvc.output.reqreply.assignment.DeleteByIdAssignmentResponse
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class DeleteByIdHandler(
    private val service: AssignmentService
) : AssignmentHandler<DeleteByIdAssignmentRequest, DeleteByIdAssignmentResponse>() {

    fun handleDelete(request: Mono<DeleteByIdAssignmentRequest>): Mono<DeleteByIdAssignmentResponse> =
        request.flatMap {
            when (it.request.requestCase) {
                AssignmentRequest.RequestCase.ASSIGNMENT_ID -> deleteById(request)
                AssignmentRequest.RequestCase.ASSIGNMENT -> unsupportedRequestTypeResponse()
                AssignmentRequest.RequestCase.REQUEST_NOT_SET -> noRequestTypeSetResponse()
            }
        }

    fun deleteById(request: Mono<DeleteByIdAssignmentRequest>): Mono<DeleteByIdAssignmentResponse> =
        request.flatMap {
            service.delete(it.request.assignmentId.id)
                .thenReturn(successDeleteResponse())
        }.doOnError { ex ->
            failureResponse(ex.javaClass.simpleName, ex.message ?: "Unknown error")
        }

    fun successDeleteResponse(): DeleteByIdAssignmentResponse =
        DeleteByIdAssignmentResponse.newBuilder().apply {
            responseBuilder.successBuilder.setMessage("Assignment deleted successfully")
        }.build()
}
