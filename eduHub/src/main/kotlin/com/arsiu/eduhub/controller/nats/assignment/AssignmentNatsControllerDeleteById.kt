package com.arsiu.eduhub.controller.nats.assignment

import com.arsiu.eduhub.controller.nats.NatsController
import com.arsiu.eduhub.service.AssignmentService
import com.arsiu.eduhub.v2.assignmentsvc.NatsSubject.ASSIGNMENT_DELETE_BY_ID
import com.arsiu.eduhub.v2.assignmentsvc.commonmodels.assignment.AssignmentProto.AssignmentResponse
import com.arsiu.eduhub.v2.assignmentsvc.commonmodels.assignment.AssignmentProto.AssignmentResponse.Failure
import com.arsiu.eduhub.v2.assignmentsvc.commonmodels.assignment.AssignmentProto.AssignmentResponse.Failure.CustomError
import com.arsiu.eduhub.v2.assignmentsvc.commonmodels.assignment.AssignmentProto.AssignmentResponse.Success
import com.arsiu.eduhub.v2.assignmentsvc.input.reqreply.assignment.DeleteByIdAssignmentRequestProto.DeleteByIdAssignmentRequest
import com.arsiu.eduhub.v2.assignmentsvc.output.reqreply.assignment.DeleteByIdAssignmentResponseProto.DeleteByIdAssignmentResponse
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

    override fun handler(request: DeleteByIdAssignmentRequest): DeleteByIdAssignmentResponse {
        return try {
            val id = request.request.assignmentId.id
            getSuccessResponse(service.delete(id))
        } catch (ex: IllegalArgumentException ){
            getFailureResponse(ex.javaClass.simpleName, ex.toString())
        }
    }

    override fun getSuccessResponse(obj: Any): DeleteByIdAssignmentResponse {
        val successResponse = Success.newBuilder()
            .setMessage("Assignment deleted successfully")
            .build()

        val response = AssignmentResponse.newBuilder()
            .setSuccess(successResponse)
            .build()

        return DeleteByIdAssignmentResponse.newBuilder()
            .setResponse(response)
            .build()
    }

    override fun getFailureResponse(exception: String, message: String): DeleteByIdAssignmentResponse {
        val customError = CustomError.newBuilder()
            .setEx(exception)
            .setMessage(message)
            .build()

        val failureResponse = Failure.newBuilder()
            .setMessage("Assignment deletion failed")
            .setErr(customError)
            .build()

        val response = AssignmentResponse.newBuilder()
            .setFailure(failureResponse)
            .build()

        return DeleteByIdAssignmentResponse.newBuilder()
            .setResponse(response)
            .build()
    }
}
