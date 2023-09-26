package com.arsiu.eduhub.controller.nats.assignment

import com.arsiu.eduhub.controller.nats.NatsController
import com.arsiu.eduhub.mapper.AssignmentNatsMapper
import com.arsiu.eduhub.service.AssignmentService
import com.arsiu.eduhub.v2.assignmentsvc.NatsSubject.ASSIGNMENT_UPDATE_BY_ID
import com.arsiu.eduhub.v2.assignmentsvc.commonmodels.assignment.AssignmentProto
import com.arsiu.eduhub.v2.assignmentsvc.commonmodels.assignment.AssignmentRequest
import com.arsiu.eduhub.v2.assignmentsvc.input.reqreply.assignment.UpdateAssignmentRequest
import com.arsiu.eduhub.v2.assignmentsvc.output.reqreply.assignment.UpdateAssignmentResponse
import com.google.protobuf.Parser
import io.nats.client.Connection
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class AssignmentNatsControllerUpdate(
    override val connection: Connection,
    private val service: AssignmentService,
    private val mapper: AssignmentNatsMapper
) : NatsController<UpdateAssignmentRequest, UpdateAssignmentResponse> {

    override val subject: String = ASSIGNMENT_UPDATE_BY_ID
    override val type = UpdateAssignmentRequest::class.java
    override val parser: Parser<UpdateAssignmentRequest> = UpdateAssignmentRequest.parser()

    override fun handler(request: UpdateAssignmentRequest): Mono<UpdateAssignmentResponse> {
        val assignmentRequest = request.request

        return when (assignmentRequest.requestCase) {
            AssignmentRequest.RequestCase.ASSIGNMENT -> processUpdate(assignmentRequest)
            AssignmentRequest.RequestCase.ASSIGNMENT_ID -> invalidAssignmentIdResponse()
            AssignmentRequest.RequestCase.REQUEST_NOT_SET -> noRequestTypeSetResponse()
        }
    }

    private fun successResponse(obj: AssignmentProto): UpdateAssignmentResponse =
        UpdateAssignmentResponse.newBuilder().apply {
            responseBuilder.successBuilder
                .setMessage("Assignment updated successfully")
                .setAssignment(obj)
        }.build()

    override fun failureResponse(exception: String, message: String): UpdateAssignmentResponse =
        UpdateAssignmentResponse.newBuilder().apply {
            responseBuilder.failureBuilder
                .setMessage("Assignment update failed")
                .errBuilder
                .setEx(exception)
                .setMessage(message)
        }.build()

    private fun invalidAssignmentIdResponse(): Mono<UpdateAssignmentResponse> =
        Mono.just(
            failureResponse("InvalidRequest", "Cannot update using AssignmentId alone")
        )

    private fun noRequestTypeSetResponse(): Mono<UpdateAssignmentResponse> =
        Mono.just(
            failureResponse("InvalidRequest", "No request type set within AssignmentRequest")
        )

    private fun processUpdate(assignmentRequest: AssignmentRequest): Mono<UpdateAssignmentResponse> =
        service.update(mapper.toEntityUpdate(assignmentRequest.assignment))
            .map { successResponse(mapper.toResponseDto(it)) }
            .onErrorResume {
                Mono.just(failureResponse(it.javaClass.simpleName, it.message ?: "Unknown error"))
            }

}
