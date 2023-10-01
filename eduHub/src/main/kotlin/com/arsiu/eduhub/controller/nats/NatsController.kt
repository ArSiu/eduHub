package com.arsiu.eduhub.controller.nats

import com.arsiu.eduhub.protobuf.handlers.Handler
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
interface NatsController<ReqT : GeneratedMessageV3, RepT : GeneratedMessageV3, H : Handler<ReqT, RepT>> {

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
     * The handler responsible for processing the incoming NATS messages. It defines how the request messages
     * of type [ReqT] should be handled and processed to produce a response of type [RepT].
     * Any class implementing this interface should provide
     * an instance of a subtype of [Handler] that corresponds to the types [ReqT] and [RepT].
     */
    val handler: H

    /**
     * Function to handle an incoming request message. Implementations should process the request
     * and return a corresponding reply message wrapped in a Mono for reactive handling.
     *
     * @param request The incoming request message of type ReqT.
     * @return A reactive publisher (Mono) that emits a single reply message of type RepT.
     */
    fun handle(request: Mono<ReqT>): Mono<RepT>

}
