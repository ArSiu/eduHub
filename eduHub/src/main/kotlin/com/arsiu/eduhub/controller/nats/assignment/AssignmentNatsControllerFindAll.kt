package com.arsiu.eduhub.controller.nats.assignment

import com.arsiu.eduhub.controller.nats.NatsController
import com.arsiu.eduhub.mapper.AssignmentNatsMapper
import com.arsiu.eduhub.service.AssignmentService
import com.arsiu.eduhub.v2.assignmentsvc.NatsSubject.ASSIGNMENT_FIND_ALL
import com.arsiu.eduhub.v2.assignmentsvc.commonmodels.assignment.AssignmentProto
import com.arsiu.eduhub.v2.assignmentsvc.commonmodels.assignment.AssignmentProto.AssignmentResponse
import com.arsiu.eduhub.v2.assignmentsvc.commonmodels.assignment.AssignmentProto.AssignmentResponse.Failure
import com.arsiu.eduhub.v2.assignmentsvc.commonmodels.assignment.AssignmentProto.AssignmentResponse.Failure.CustomError
import com.arsiu.eduhub.v2.assignmentsvc.commonmodels.assignment.AssignmentProto.AssignmentResponse.Success
import com.arsiu.eduhub.v2.assignmentsvc.input.reqreply.assignment.FindAllAssignmentRequestProto.FindAllAssignmentRequest
import com.arsiu.eduhub.v2.assignmentsvc.output.reqreply.assignment.FindAllAssignmentResponseProto.FindAllAssignmentResponse
import com.google.protobuf.Parser
import io.nats.client.Connection
import org.springframework.stereotype.Component

@Component
class AssignmentNatsControllerFindAll(
    override val connection: Connection,
    private val service: AssignmentService,
    private val mapper: AssignmentNatsMapper
) : NatsController<FindAllAssignmentRequest, FindAllAssignmentResponse> {

    override val subject: String = ASSIGNMENT_FIND_ALL
    override val parser: Parser<FindAllAssignmentRequest> = FindAllAssignmentRequest.parser()

    override fun handler(request: FindAllAssignmentRequest): FindAllAssignmentResponse {
        return try {
            getSuccessResponse(request)
        } catch (ex: IllegalArgumentException ){
            getFailureResponse(ex.javaClass.simpleName, ex.toString())
        }
    }

    override fun getSuccessResponse(obj: Any): FindAllAssignmentResponse {
        val a = mapper.toResponseDtoList(service.findAll())

        val assignments = AssignmentProto.Assignments.newBuilder().addAllAssignments(a)

        val successResponse = Success.newBuilder()
            .setMessage("Assignments returned successfully")
            .setAssignments(assignments)
            .build()

        val response = AssignmentResponse.newBuilder()
            .setSuccess(successResponse)
            .build()

        return FindAllAssignmentResponse.newBuilder()
            .setResponse(response)
            .build()
    }

    override fun getFailureResponse(exception: String, message: String): FindAllAssignmentResponse{
        val customError = CustomError.newBuilder()
            .setEx(exception)
            .setMessage(message)
            .build()

        val failureResponse = Failure.newBuilder()
            .setMessage("Assignments return failed")
            .setErr(customError)
            .build()

        val response = AssignmentResponse.newBuilder()
            .setFailure(failureResponse)
            .build()

        return FindAllAssignmentResponse.newBuilder()
            .setResponse(response)
            .build()
    }
}
