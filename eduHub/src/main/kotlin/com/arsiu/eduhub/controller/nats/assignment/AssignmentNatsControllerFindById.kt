package com.arsiu.eduhub.controller.nats.assignment

import com.arsiu.eduhub.controller.nats.NatsController
import com.arsiu.eduhub.mapper.AssignmentNatsMapper
import com.arsiu.eduhub.service.AssignmentService
import com.arsiu.eduhub.v2.assignmentsvc.NatsSubject.ASSIGNMENT_BY_ID
import com.arsiu.eduhub.v2.assignmentsvc.commonmodels.assignment.AssignmentProto
import com.arsiu.eduhub.v2.assignmentsvc.commonmodels.assignment.AssignmentProto.AssignmentResponse
import com.arsiu.eduhub.v2.assignmentsvc.commonmodels.assignment.AssignmentProto.AssignmentResponse.Failure
import com.arsiu.eduhub.v2.assignmentsvc.commonmodels.assignment.AssignmentProto.AssignmentResponse.Failure.CustomError
import com.arsiu.eduhub.v2.assignmentsvc.commonmodels.assignment.AssignmentProto.AssignmentResponse.Success
import com.arsiu.eduhub.v2.assignmentsvc.input.reqreply.assignment.FindByIdAssignmentRequestProto.FindByIdAssignmentRequest
import com.arsiu.eduhub.v2.assignmentsvc.output.reqreply.assignment.FindByIdAssignmentResponseProto.FindByIdAssignmentResponse
import com.google.protobuf.Parser
import io.nats.client.Connection
import org.springframework.stereotype.Component

@Component
class AssignmentNatsControllerFindById(
    override val connection: Connection,
    private val service: AssignmentService,
    private val mapper: AssignmentNatsMapper
) : NatsController<FindByIdAssignmentRequest, FindByIdAssignmentResponse> {

    override val subject: String = ASSIGNMENT_BY_ID
    override val parser: Parser<FindByIdAssignmentRequest> = FindByIdAssignmentRequest.parser()

    override fun handler(request: FindByIdAssignmentRequest): FindByIdAssignmentResponse {
        return try {
            val id = request.request.assignmentId.id.toString()
            val find = service.findById(id)
            getSuccessResponse(mapper.toResponseDto(find))
        } catch (ex: IllegalArgumentException ){
            getFailureResponse(ex.javaClass.simpleName, ex.toString())
        }
    }

    override fun getSuccessResponse(obj: Any): FindByIdAssignmentResponse{
        val successResponse = Success.newBuilder()
            .setMessage("Assignment find successfully")
            .setAssignment(obj as AssignmentProto.Assignment)
            .build()

        val response = AssignmentResponse.newBuilder()
            .setSuccess(successResponse)
            .build()

        return FindByIdAssignmentResponse.newBuilder()
            .setResponse(response)
            .build()
    }

    override fun getFailureResponse(exception: String, message: String): FindByIdAssignmentResponse {
        val customError = CustomError.newBuilder()
            .setEx(exception)
            .setMessage(message)
            .build()

        val failureResponse = Failure.newBuilder()
            .setMessage("Assignment find failed")
            .setErr(customError)
            .build()

        val response = AssignmentResponse.newBuilder()
            .setFailure(failureResponse)
            .build()

        return FindByIdAssignmentResponse.newBuilder()
            .setResponse(response)
            .build()
    }
}
