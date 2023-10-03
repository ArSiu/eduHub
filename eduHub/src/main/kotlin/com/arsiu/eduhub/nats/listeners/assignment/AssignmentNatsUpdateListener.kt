package com.arsiu.eduhub.nats.listeners.assignment

import com.arsiu.eduhub.nats.listeners.NatsListener
import com.arsiu.eduhub.protobuf.handlers.assignment.FindAllStreamHandler
import com.arsiu.eduhub.shared.streams.SharedAssignmentStream
import com.arsiu.eduhub.v2.assignmentsvc.NatsSubject.ASSIGNMENT_UPDATE_BUS
import com.arsiu.eduhub.v2.assignmentsvc.commonmodels.assignment.AssignmentProto
import com.google.protobuf.Parser
import io.nats.client.Connection
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class AssignmentNatsUpdateListener(
    override val connection: Connection,
    val findAllStreamHandler: FindAllStreamHandler,
    val sharedAssignmentStream: SharedAssignmentStream
) : NatsListener<AssignmentProto, AssignmentProto> {

    override val subject = ASSIGNMENT_UPDATE_BUS
    override val type = AssignmentProto::class.java
    override val parser: Parser<AssignmentProto> = AssignmentProto.parser()

    private val logger: Logger = LoggerFactory.getLogger(AssignmentNatsUpdateListener::class.java)

    /**
     * Handle an incoming AssignmentProto request, convert it to a FindAllAssignmentStreamResponse,
     * and then push it to the sharedAssignmentStream for broadcasting.
     *
     * @param request The incoming AssignmentProto request.
     */
    override fun handle(request: AssignmentProto) {
        Mono.just(request)
            .map(findAllStreamHandler::convertToFindAllStreamResponse)
            .doOnNext { response -> sharedAssignmentStream.push(listOf(response)) }
            .doOnError { e -> logger.error("Error handling NATS update for assignment", e) }
            .subscribe()
    }

}
