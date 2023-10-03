package com.arsiu.eduhub.controller.nats.assignment

import com.arsiu.eduhub.controller.nats.NatsController
import com.arsiu.eduhub.protobuf.handlers.assignment.UpdateHandler
import com.arsiu.eduhub.v2.assignmentsvc.NatsSubject.ASSIGNMENT_UPDATE_BY_ID
import com.arsiu.eduhub.v2.assignmentsvc.input.reqreply.assignment.UpdateAssignmentRequest
import com.arsiu.eduhub.v2.assignmentsvc.output.reqreply.assignment.UpdateAssignmentResponse
import com.google.protobuf.Parser
import io.nats.client.Connection
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class AssignmentNatsControllerUpdate(
    override val connection: Connection,
    override val handler: UpdateHandler
) : NatsController<UpdateAssignmentRequest, UpdateAssignmentResponse, UpdateHandler> {

    override val subject: String = ASSIGNMENT_UPDATE_BY_ID
    override val type = UpdateAssignmentRequest::class.java
    override val parser: Parser<UpdateAssignmentRequest> = UpdateAssignmentRequest.parser()

    override fun handle(request: UpdateAssignmentRequest): Mono<UpdateAssignmentResponse> =
        handler.handleUpdate(request)

}
