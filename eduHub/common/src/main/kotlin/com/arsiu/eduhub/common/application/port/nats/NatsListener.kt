package com.arsiu.eduhub.common.application.port.nats

import com.google.protobuf.GeneratedMessageV3

/**
 * `NatsListener` is an interface that models components responsible for listening to and handling
 * messages from NATS (a messaging system). Any class that implements this interface is expected
 * to handle incoming messages of type `ReqT` and produce replies of type `RepT`.
 *
 * @param ReqT The type of the request message, which must be a subclass of `GeneratedMessageV3` from Protocol Buffers.
 * @param RepT The type of the reply message, which must also be a subclass of `GeneratedMessageV3`.
 *
 * This interface extends `NatsComponent<ReqT, RepT>` (not provided in the snippet) which presumably provides
 * additional functionalities or definitions related to NATS components.
 */
interface NatsListener<ReqT : GeneratedMessageV3, RepT : GeneratedMessageV3> :
    NatsComponent<ReqT, RepT> {

    /**
     * This function defines how to handle an incoming request message. Implementing classes should
     * process the provided request and produce a corresponding reply. Since the response is expected
     * to be reactive, the result is wrapped inside a `Mono` publisher which emits a single reply item.
     *
     * @param request The incoming request message of type `ReqT`.
     */
    fun handle(request: ReqT)
}
