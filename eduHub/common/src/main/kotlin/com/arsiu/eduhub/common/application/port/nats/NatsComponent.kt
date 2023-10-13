package com.arsiu.eduhub.common.application.port.nats

import com.google.protobuf.GeneratedMessageV3
import com.google.protobuf.Parser
import io.nats.client.Connection

interface NatsComponent<ReqT : GeneratedMessageV3, RepT : GeneratedMessageV3> {
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

}
