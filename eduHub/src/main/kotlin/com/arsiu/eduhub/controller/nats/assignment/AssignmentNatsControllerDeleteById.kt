package com.arsiu.eduhub.controller.nats.assignment

import com.arsiu.eduhub.controller.nats.NatsController
import com.arsiu.eduhub.service.AssignmentService
import com.arsiu.eduhub.v2.assignmentsvc.NatsSubject.ASSIGNMENT_DELETE_BY_ID
import com.arsiu.eduhub.v2.assignmentsvc.input.reqreply.assignment.DeleteByIdAssignmentRequest
import com.arsiu.eduhub.v2.assignmentsvc.output.reqreply.assignment.DeleteByIdAssignmentResponse
import com.google.protobuf.Parser
import io.nats.client.Connection
import org.springframework.stereotype.Component

@Component
class AssignmentNatsControllerDeleteById(
    override val connection: Connection,
    private val service: AssignmentService
) : NatsController<DeleteByIdAssignmentRequest, DeleteByIdAssignmentResponse> {

    override val subject: String = ASSIGNMENT_DELETE_BY_ID
    override val parser: Parser<DeleteByIdAssignmentRequest> = DeleteByIdAssignmentRequest.parser()

    override fun handler(request: DeleteByIdAssignmentRequest): DeleteByIdAssignmentResponse =
        runCatching {
            val id = request.request.assignmentId.id
            service.delete(id).block()
            getSuccessResponse()
        }.getOrElse { ex ->
            getFailureResponse(ex.javaClass.simpleName, ex.toString())
        }

    fun getSuccessResponse(): DeleteByIdAssignmentResponse =
        DeleteByIdAssignmentResponse.newBuilder().apply {
            responseBuilder.successBuilder
                .setMessage("Assignment deleted successfully")
        }.build()

    fun getFailureResponse(exception: String, message: String): DeleteByIdAssignmentResponse =
        DeleteByIdAssignmentResponse.newBuilder().apply {
            responseBuilder.failureBuilder
                .setMessage("Assignment deletion failed")
                .errBuilder
                .setEx(exception)
                .setMessage(message)
        }.build()

}
