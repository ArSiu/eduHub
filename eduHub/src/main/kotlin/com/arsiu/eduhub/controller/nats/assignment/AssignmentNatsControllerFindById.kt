package com.arsiu.eduhub.controller.nats.assignment

import com.arsiu.eduhub.controller.nats.NatsController
import com.arsiu.eduhub.mapper.AssignmentNatsMapper
import com.arsiu.eduhub.service.AssignmentService
import com.arsiu.eduhub.v2.assignmentsvc.NatsSubject.ASSIGNMENT_BY_ID
import com.arsiu.eduhub.v2.assignmentsvc.commonmodels.assignment.AssignmentProto
import com.arsiu.eduhub.v2.assignmentsvc.commonmodels.assignment.AssignmentRequest
import com.arsiu.eduhub.v2.assignmentsvc.input.reqreply.assignment.FindByIdAssignmentRequest
import com.arsiu.eduhub.v2.assignmentsvc.output.reqreply.assignment.FindByIdAssignmentResponse
import com.google.protobuf.Parser
import io.nats.client.Connection
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class AssignmentNatsControllerFindById(
    override val connection: Connection,
    private val service: AssignmentService,
    private val mapper: AssignmentNatsMapper
) : NatsController<FindByIdAssignmentRequest, FindByIdAssignmentResponse> {

    override val subject: String = ASSIGNMENT_BY_ID
    override val type = FindByIdAssignmentRequest::class.java
    override val parser: Parser<FindByIdAssignmentRequest> = FindByIdAssignmentRequest.parser()

    override fun handler(request: FindByIdAssignmentRequest): Mono<FindByIdAssignmentResponse> {

        val assignmentRequest = request.request

        return when (assignmentRequest.requestCase) {
            AssignmentRequest.RequestCase.ASSIGNMENT_ID -> processFind(assignmentRequest)
            AssignmentRequest.RequestCase.ASSIGNMENT -> unsupportedRequestTypeResponse()
            AssignmentRequest.RequestCase.REQUEST_NOT_SET -> noRequestTypeSetResponse()
        }
    }

    private fun successResponse(obj: AssignmentProto): FindByIdAssignmentResponse =
        FindByIdAssignmentResponse.newBuilder().apply {
            responseBuilder.successBuilder
                .setAssignment(obj)
                .setMessage("Assignment found successfully")
        }.build()

    override fun failureResponse(exception: String, message: String): FindByIdAssignmentResponse =
        FindByIdAssignmentResponse.newBuilder().apply {
            responseBuilder.failureBuilder
                .setMessage("Assignment find failed")
                .errBuilder
                .setEx(exception)
                .setMessage(message)
        }.build()

    private fun unsupportedRequestTypeResponse(): Mono<FindByIdAssignmentResponse> =
        Mono.just(
            failureResponse("UnsupportedRequestType", "AssignmentProto is not supported for search")
        )

    private fun noRequestTypeSetResponse(): Mono<FindByIdAssignmentResponse> =
        Mono.just(
            failureResponse("InvalidRequest", "No request type set within AssignmentRequest")
        )

    private fun processFind(assignmentRequest: AssignmentRequest): Mono<FindByIdAssignmentResponse> =
        service.findById(assignmentRequest.assignmentId.id.toString())
            .map { successResponse(mapper.toResponseDto(it)) }
            .onErrorResume {
                Mono.just(failureResponse(it.javaClass.simpleName, it.message ?: "Unknown error"))
            }

}
