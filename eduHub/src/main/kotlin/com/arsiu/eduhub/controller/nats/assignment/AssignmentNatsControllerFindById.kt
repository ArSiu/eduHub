package com.arsiu.eduhub.controller.nats.assignment

import com.arsiu.eduhub.controller.nats.NatsController
import com.arsiu.eduhub.mapper.AssignmentNatsMapper
import com.arsiu.eduhub.service.AssignmentService
import com.arsiu.eduhub.v2.assignmentsvc.NatsSubject.ASSIGNMENT_BY_ID
import com.arsiu.eduhub.v2.assignmentsvc.commonmodels.assignment.AssignmentProto
import com.arsiu.eduhub.v2.assignmentsvc.input.reqreply.assignment.FindByIdAssignmentRequest
import com.arsiu.eduhub.v2.assignmentsvc.output.reqreply.assignment.FindByIdAssignmentResponse
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

    override fun handler(request: FindByIdAssignmentRequest): FindByIdAssignmentResponse =
        runCatching {
            val id = request.request.assignmentId.id.toString()
            val find = service.findById(id)
            getSuccessResponse(mapper.toResponseDto(find))
        }.getOrElse { ex ->
            getFailureResponse(ex.javaClass.simpleName, ex.toString())
        }

    private fun getSuccessResponse(obj: AssignmentProto): FindByIdAssignmentResponse =
        FindByIdAssignmentResponse.newBuilder().apply {
            responseBuilder.successBuilder
                .setAssignment(obj)
                .setMessage("Assignment find successfully")
        }.build()

    private fun getFailureResponse(exception: String, message: String): FindByIdAssignmentResponse =
        FindByIdAssignmentResponse.newBuilder().apply {
            responseBuilder.failureBuilder
                .setMessage("Assignment find failed")
                .errBuilder
                .setEx(exception)
                .setMessage(message)
        }.build()

}
