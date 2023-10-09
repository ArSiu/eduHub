package com.arsiu.eduhub.assignment.infrastructure.shared.streams

import com.arsiu.eduhub.v2.assignmentsvc.output.reqreply.assignment.FindAllAssignmentStreamResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Sinks

/**
 * This class is responsible for managing a shared assignment stream.
 * It uses Reactor's Sink API to multicast assignment updates to multiple subscribers.
 */
@Component
class SharedAssignmentStream {

    private val logger: Logger = LoggerFactory.getLogger(SharedAssignmentStream::class.java)

    // Sink that can hold multiple subscribers. It uses a backpressure buffer to handle
    // situations where items are being produced faster than they can be consumed.
    private val sink =
        Sinks.many().multicast().onBackpressureBuffer<FindAllAssignmentStreamResponse>()

    // Expose the processor as a Flux so that multiple subscribers
    // can listen for assignment updates.
    val flux: Flux<FindAllAssignmentStreamResponse> = sink.asFlux().publish().autoConnect(1)

    /**
     * Pushes new assignment updates to the shared stream.
     *
     * @param assignments List of assignment updates to be emitted to the shared stream.
     */
    fun push(assignments: List<FindAllAssignmentStreamResponse>) {
        assignments.forEach { assignment ->
            val result = sink.tryEmitNext(assignment)
            if (result.isFailure) {
                logger.warn("Failed to emit assignment: $assignment, Reason: ${result.isFailure}")
            }
        }
    }
}
