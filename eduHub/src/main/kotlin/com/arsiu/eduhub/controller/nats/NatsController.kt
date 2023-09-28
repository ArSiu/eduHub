package com.arsiu.eduhub.controller.nats

import com.google.protobuf.GeneratedMessageV3
import com.google.protobuf.Parser
import io.nats.client.Connection
import reactor.core.publisher.Mono

/**
 * A generic interface for NATS controllers that utilize Protocol Buffers for message handling.
 * This interface provides a standard contract for processing NATS messages with Protocol Buffers serialization.
 *
 * @param ReqT The type of the request message which should be a Protocol Buffers message.
 * @param RepT The type of the reply message which should also be a Protocol Buffers message.
 */
interface NatsController<ReqT : GeneratedMessageV3, RepT : GeneratedMessageV3> {

    /**
     * Represents the NATS connection which is utilized for communication with NATS servers.
     */
    val connection: Connection

    /**
     * The class type of the request message. This can be used for type checks or any type-specific operations.
     */
    val type: Class<ReqT>

    /**
     * A parser that can be used to deserialize the incoming raw NATS message
     * into a Protocol Buffers message of type ReqT.
     */
    val parser: Parser<ReqT>

    /**
     * The NATS subject that this controller is interested in. This is the topic to which this controller will
     * publish messages and also potentially subscribe to for listening to incoming messages.
     */
    val subject: String

    /**
     * Function to handle an incoming request message. Implementations should process the request
     * and return a corresponding reply message wrapped in a Mono for reactive handling.
     *
     * @param message The incoming request message of type ReqT.
     * @return A reactive publisher (Mono) that emits a single reply message of type RepT.
     */
    fun handler(request: ReqT): Mono<RepT>

    /**
     * Provides a standard mechanism to construct a reply message in case of failures or exceptions.
     * This can be used for sending error responses to the client.
     *
     * @param exception The exception name or type that occurred.
     * @param message A descriptive error message to be sent in the reply.
     * @return A reply message of type RepT representing the error or failure.
     */
    fun failureResponse(exception: String, message: String): RepT
}
