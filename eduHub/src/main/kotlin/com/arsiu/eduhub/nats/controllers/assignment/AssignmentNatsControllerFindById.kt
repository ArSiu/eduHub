package com.arsiu.eduhub.nats.controllers.assignment

import com.arsiu.eduhub.nats.controllers.NatsController
import com.arsiu.eduhub.protobuf.handlers.assignment.FindByIdHandler
import com.arsiu.eduhub.v2.assignmentsvc.NatsSubject.ASSIGNMENT_BY_ID
import com.arsiu.eduhub.v2.assignmentsvc.input.reqreply.assignment.FindByIdAssignmentRequest
import com.arsiu.eduhub.v2.assignmentsvc.output.reqreply.assignment.FindByIdAssignmentResponse
import com.google.protobuf.Parser
import io.nats.client.Connection
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class AssignmentNatsControllerFindById(
    override val connection: Connection,
    override val handler: FindByIdHandler
) : NatsController<FindByIdAssignmentRequest, FindByIdAssignmentResponse, FindByIdHandler> {

    override val subject: String = ASSIGNMENT_BY_ID
    override val type = FindByIdAssignmentRequest::class.java
    override val parser: Parser<FindByIdAssignmentRequest> = FindByIdAssignmentRequest.parser()

    override fun handle(request: FindByIdAssignmentRequest): Mono<FindByIdAssignmentResponse> =
        handler.handleFindById(request)

}
