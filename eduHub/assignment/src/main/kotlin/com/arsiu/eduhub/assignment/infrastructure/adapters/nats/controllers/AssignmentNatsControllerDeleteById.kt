package com.arsiu.eduhub.assignment.infrastructure.adapters.nats.controllers

import com.arsiu.eduhub.assignment.infrastructure.adapters.handlers.assignment.DeleteByIdHandler
import com.arsiu.eduhub.common.infrastructure.adapters.nats.NatsController
import com.arsiu.eduhub.v2.assignmentsvc.NatsSubject.ASSIGNMENT_DELETE_BY_ID
import com.arsiu.eduhub.v2.assignmentsvc.input.reqreply.assignment.DeleteByIdAssignmentRequest
import com.arsiu.eduhub.v2.assignmentsvc.output.reqreply.assignment.DeleteByIdAssignmentResponse
import com.google.protobuf.Parser
import io.nats.client.Connection
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class AssignmentNatsControllerDeleteById(
    override val connection: Connection,
    override val handler: DeleteByIdHandler
) : NatsController<DeleteByIdAssignmentRequest, DeleteByIdAssignmentResponse, DeleteByIdHandler> {

    override val subject: String = ASSIGNMENT_DELETE_BY_ID
    override val type = DeleteByIdAssignmentRequest::class.java
    override val parser: Parser<DeleteByIdAssignmentRequest> = DeleteByIdAssignmentRequest.parser()

    override fun handle(request: DeleteByIdAssignmentRequest): Mono<DeleteByIdAssignmentResponse> =
        handler.handleDelete(request)

}
