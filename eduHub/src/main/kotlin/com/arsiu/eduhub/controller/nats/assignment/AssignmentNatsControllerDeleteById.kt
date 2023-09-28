package com.arsiu.eduhub.controller.nats.assignment

import com.arsiu.eduhub.controller.nats.NatsController
import com.arsiu.eduhub.service.AssignmentService
import com.arsiu.eduhub.v2.assignmentsvc.NatsSubject.ASSIGNMENT_DELETE_BY_ID
import com.arsiu.eduhub.v2.assignmentsvc.commonmodels.assignment.AssignmentRequest
import com.arsiu.eduhub.v2.assignmentsvc.input.reqreply.assignment.DeleteByIdAssignmentRequest
import com.arsiu.eduhub.v2.assignmentsvc.output.reqreply.assignment.DeleteByIdAssignmentResponse
import com.google.protobuf.Parser
import io.nats.client.Connection
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class AssignmentNatsControllerDeleteById(
    override val connection: Connection,
    private val service: AssignmentService
) : NatsController<DeleteByIdAssignmentRequest, DeleteByIdAssignmentResponse> {

    override val subject: String = ASSIGNMENT_DELETE_BY_ID
    override val type = DeleteByIdAssignmentRequest::class.java
    override val parser: Parser<DeleteByIdAssignmentRequest> = DeleteByIdAssignmentRequest.parser()

    override fun handler(request: DeleteByIdAssignmentRequest): Mono<DeleteByIdAssignmentResponse> {

        val assignmentRequest = request.request

        return when (assignmentRequest.requestCase) {
            AssignmentRequest.RequestCase.ASSIGNMENT_ID -> processDeletion(assignmentRequest)
            AssignmentRequest.RequestCase.ASSIGNMENT -> unsupportedRequestTypeResponse()
            AssignmentRequest.RequestCase.REQUEST_NOT_SET -> noRequestTypeSetResponse()
        }
    }

    private fun successResponse(): DeleteByIdAssignmentResponse =
        DeleteByIdAssignmentResponse.newBuilder().apply {
            responseBuilder.successBuilder.setMessage("Assignment deleted successfully")
        }.build()

    override fun failureResponse(exception: String, message: String): DeleteByIdAssignmentResponse =
        DeleteByIdAssignmentResponse.newBuilder().apply {
            responseBuilder.failureBuilder
                .setMessage("Assignment deletion failed")
                .errBuilder
                .setEx(exception)
                .setMessage(message)
        }.build()

    private fun unsupportedRequestTypeResponse(): Mono<DeleteByIdAssignmentResponse> =
        Mono.just(
            failureResponse("UnsupportedRequestType", "AssignmentProto is not supported for deletion")
        )

    private fun noRequestTypeSetResponse(): Mono<DeleteByIdAssignmentResponse> =
        Mono.just(
            failureResponse("InvalidRequest", "No request type set within AssignmentRequest")
        )

    private fun processDeletion(assignmentRequest: AssignmentRequest): Mono<DeleteByIdAssignmentResponse> =
        Mono.defer {
            val id = assignmentRequest.assignmentId.id
            service.delete(id).thenReturn(successResponse())
        }.onErrorResume {
            Mono.just(failureResponse(it.javaClass.simpleName, it.message ?: "Unknown error"))
        }

}
