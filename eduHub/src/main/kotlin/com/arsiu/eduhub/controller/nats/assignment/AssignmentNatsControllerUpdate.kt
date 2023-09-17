package com.arsiu.eduhub.controller.nats.assignment

import com.arsiu.eduhub.controller.nats.NatsController
import com.arsiu.eduhub.exception.NotFoundException
import com.arsiu.eduhub.mapper.AssignmentNatsMapper
import com.arsiu.eduhub.service.AssignmentService
import com.arsiu.eduhub.v2.assignmentsvc.NatsSubject.ASSIGNMENT_UPDATE_BY_ID
import com.arsiu.eduhub.v2.assignmentsvc.commonmodels.assignment.AssignmentProto
import com.arsiu.eduhub.v2.assignmentsvc.commonmodels.assignment.AssignmentProto.AssignmentResponse
import com.arsiu.eduhub.v2.assignmentsvc.commonmodels.assignment.AssignmentProto.AssignmentResponse.Failure
import com.arsiu.eduhub.v2.assignmentsvc.commonmodels.assignment.AssignmentProto.AssignmentResponse.Failure.CustomError
import com.arsiu.eduhub.v2.assignmentsvc.commonmodels.assignment.AssignmentProto.AssignmentResponse.Success
import com.arsiu.eduhub.v2.assignmentsvc.input.reqreply.assignment.UpdateAssignmentRequestProto.UpdateAssignmentRequest
import com.arsiu.eduhub.v2.assignmentsvc.output.reqreply.assignment.UpdateAssignmentResponseProto.UpdateAssignmentResponse
import com.google.protobuf.Parser
import io.nats.client.Connection
import org.springframework.stereotype.Component

@Component
class AssignmentNatsControllerUpdate(
    override val connection: Connection,
    private val service: AssignmentService,
    private val mapper: AssignmentNatsMapper
) : NatsController<UpdateAssignmentRequest, UpdateAssignmentResponse> {

    override val subject: String = ASSIGNMENT_UPDATE_BY_ID
    override val parser: Parser<UpdateAssignmentRequest> = UpdateAssignmentRequest.parser()

    override fun handler(request: UpdateAssignmentRequest): UpdateAssignmentResponse {
        return try {
            val obj = mapper.toEntityUpdate(request.request.assignment)
            val updated = service.update(obj)
            getSuccessResponse(mapper.toResponseDto(updated))
        } catch (ex: NotFoundException){
            getFailureResponse(ex.javaClass.simpleName, ex.toString())
        }
    }

    override fun getSuccessResponse(obj: Any): UpdateAssignmentResponse {
        val successResponse = Success.newBuilder()
            .setMessage("Assignment updated successfully")
            .setAssignment(obj as AssignmentProto.Assignment)
            .build()

        val response = AssignmentResponse.newBuilder()
            .setSuccess(successResponse)
            .build()

        return UpdateAssignmentResponse.newBuilder()
            .setResponse(response)
            .build()
    }

    override fun getFailureResponse(exception: String, message: String): UpdateAssignmentResponse {
        val customError = CustomError.newBuilder()
            .setEx(exception)
            .setMessage(message)
            .build()

        val failureResponse = Failure.newBuilder()
            .setMessage("Assignment updated failed")
            .setErr(customError)
            .build()

        val response = AssignmentResponse.newBuilder()
            .setFailure(failureResponse)
            .build()

        return UpdateAssignmentResponse.newBuilder()
            .setResponse(response)
            .build()
    }
}
