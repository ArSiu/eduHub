package com.arsiu.eduhub.assignment.infrastructure.adapters.nats.controllers

import com.arsiu.eduhub.assignment.infrastructure.adapters.handlers.assignment.FindAllHandler
import com.arsiu.eduhub.common.infrastructure.adapters.nats.NatsController
import com.arsiu.eduhub.v2.assignmentsvc.NatsSubject.ASSIGNMENT_FIND_ALL
import com.arsiu.eduhub.v2.assignmentsvc.input.reqreply.assignment.FindAllAssignmentRequest
import com.arsiu.eduhub.v2.assignmentsvc.output.reqreply.assignment.FindAllAssignmentResponse
import com.google.protobuf.Parser
import io.nats.client.Connection
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class AssignmentNatsControllerFindAll(
    override val connection: Connection,
    override val handler: FindAllHandler
) : NatsController<FindAllAssignmentRequest, FindAllAssignmentResponse, FindAllHandler> {

    override val subject: String = ASSIGNMENT_FIND_ALL
    override val type = FindAllAssignmentRequest::class.java
    override val parser: Parser<FindAllAssignmentRequest> = FindAllAssignmentRequest.parser()

    override fun handle(request: FindAllAssignmentRequest): Mono<FindAllAssignmentResponse> =
        handler.handleFindAll(request)

}
