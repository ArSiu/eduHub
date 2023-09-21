package com.arsiu.eduhub.controller.nats.assignment

import com.arsiu.eduhub.controller.nats.NatsController
import com.arsiu.eduhub.mapper.AssignmentNatsMapper
import com.arsiu.eduhub.service.AssignmentService
import com.arsiu.eduhub.v2.assignmentsvc.NatsSubject.ASSIGNMENT_FIND_ALL
import com.arsiu.eduhub.v2.assignmentsvc.input.reqreply.assignment.FindAllAssignmentRequest
import com.arsiu.eduhub.v2.assignmentsvc.output.reqreply.assignment.FindAllAssignmentResponse
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

    override fun handler(request: FindAllAssignmentRequest): FindAllAssignmentResponse =
        runCatching {
            getSuccessResponse()
        }.getOrElse { ex ->
            getFailureResponse(ex.javaClass.simpleName, ex.toString())
        }

    private fun getSuccessResponse(): FindAllAssignmentResponse =
        FindAllAssignmentResponse.newBuilder().apply {
            responseBuilder.successBuilder
                .setMessage("Assignments returned successfully")
                .assignmentsBuilder
                .addAllAssignment(mapper.toResponseDtoList(service.findAll().collectList().block()!!))
        }.build()


    private fun getFailureResponse(exception: String, message: String): FindAllAssignmentResponse =
        FindAllAssignmentResponse.newBuilder().apply {
            responseBuilder.failureBuilder
                .setMessage("Assignments return failed")
                .errBuilder
                .setEx(exception)
                .setMessage(message)
        }.build()

}
