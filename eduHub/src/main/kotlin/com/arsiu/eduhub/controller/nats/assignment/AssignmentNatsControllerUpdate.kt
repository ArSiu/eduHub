package com.arsiu.eduhub.controller.nats.assignment

import com.arsiu.eduhub.controller.nats.NatsController
import com.arsiu.eduhub.mapper.AssignmentNatsMapper
import com.arsiu.eduhub.service.AssignmentService
import com.arsiu.eduhub.v2.assignmentsvc.NatsSubject.ASSIGNMENT_UPDATE_BY_ID
import com.arsiu.eduhub.v2.assignmentsvc.commonmodels.assignment.AssignmentProto
import com.arsiu.eduhub.v2.assignmentsvc.input.reqreply.assignment.UpdateAssignmentRequest
import com.arsiu.eduhub.v2.assignmentsvc.output.reqreply.assignment.UpdateAssignmentResponse
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

    override fun handler(request: UpdateAssignmentRequest): UpdateAssignmentResponse = runCatching {
        val obj = mapper.toEntityUpdate(request.request.assignment)
        val updated = service.update(obj).block()!!
        getSuccessResponse(mapper.toResponseDto(updated))
    }.getOrElse { ex ->
        getFailureResponse(ex.javaClass.simpleName, ex.toString())
    }

    private fun getSuccessResponse(obj: AssignmentProto): UpdateAssignmentResponse =
        UpdateAssignmentResponse.newBuilder().apply {
            responseBuilder.successBuilder.setMessage("Assignment updated successfully").setAssignment(obj)
        }.build()

    private fun getFailureResponse(exception: String, message: String): UpdateAssignmentResponse =
        UpdateAssignmentResponse.newBuilder().apply {
            responseBuilder.failureBuilder.setMessage("Assignment updated failed").errBuilder.setEx(exception)
                .setMessage(message)
        }.build()


}
