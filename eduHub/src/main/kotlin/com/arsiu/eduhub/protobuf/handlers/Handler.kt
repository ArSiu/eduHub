package com.arsiu.eduhub.protobuf.handlers

import com.arsiu.eduhub.v2.assignmentsvc.commonmodels.assignment.AssignmentResponse
import com.google.protobuf.GeneratedMessageV3
import reactor.core.publisher.Mono

interface Handler <ReqT : GeneratedMessageV3, RepT : GeneratedMessageV3>{

    fun failureResponse(exception: String, message: String): RepT

    fun invalidIdResponse(): Mono<RepT>

    fun noRequestTypeSetResponse(): Mono<RepT>

    fun unsupportedRequestTypeResponse(): Mono<RepT>
}
