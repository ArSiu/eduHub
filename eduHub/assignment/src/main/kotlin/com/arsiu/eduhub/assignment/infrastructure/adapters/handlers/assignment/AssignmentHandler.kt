package com.arsiu.eduhub.assignment.infrastructure.adapters.handlers.assignment

import com.arsiu.eduhub.common.infrastructure.adapters.protobuf.handler.Handler
import com.arsiu.eduhub.v2.assignmentsvc.commonmodels.assignment.AssignmentResponse
import com.google.protobuf.GeneratedMessageV3
import reactor.core.publisher.Mono

open class AssignmentHandler<ReqT : GeneratedMessageV3, RepT : GeneratedMessageV3> :
    Handler<ReqT, RepT> {

    override fun failureResponse(exception: String, message: String): RepT =
        AssignmentResponse.newBuilder().apply {
            failureBuilder
                .setMessage(message)
                .errBuilder
                .setEx(exception)
                .setMessage(message)
        }.build() as RepT

    override fun invalidIdResponse(): Mono<RepT> =
        Mono.just(
            failureResponse(
                "InvalidRequest",
                "AssignmentId is not supported"
            )
        )

    override fun noRequestTypeSetResponse(): Mono<RepT> =
        Mono.just(
            failureResponse(
                "InvalidRequest",
                "No request type set within AssignmentRequest"
            )
        )

    override fun unsupportedRequestTypeResponse(): Mono<RepT> =
        Mono.just(
            failureResponse(
                "UnsupportedRequestType",
                "AssignmentProto is not supported"
            )
        )
}
