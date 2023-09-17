package com.arsiu.eduhub.controller.nats

import com.google.protobuf.GeneratedMessageV3
import com.google.protobuf.Parser
import io.nats.client.Connection

/**
 * A generic interface for NATS controllers that handle messages using Protocol Buffers.
 *
 * @param ReqT The type of the request message.
 * @param RepT The type of the reply message.
 */
interface NatsController<ReqT :  GeneratedMessageV3, RepT : GeneratedMessageV3> {

    /**
     * The NATS connection used for communication.
     */
    val connection: Connection

    /**
     * The NATS subject to publish and subscribe to.
     */
    val subject: String

    /**
     * The Protocol Buffers parser to parse bytes to proto and visa verse.
     */
    val parser: Parser<ReqT>

    /**
     * Handles an incoming request message and returns a reply message.
     *
     * @param request The incoming request message.
     * @return The reply message.
     */
    fun handler(request: ReqT): RepT

    /**
     * Generates a success response message based on the provided object.
     *
     * @param obj The object to include in the response message.
     * @return The success response message.
     */
    fun getSuccessResponse(obj: Any): RepT

    /**
     * Generates a failure response message with the specified exception and message.
     *
     * @param exception The exception type or identifier.
     * @param message The error message.
     * @return The failure response message.
     */
    fun getFailureResponse(exception: String, message: String): RepT

}
