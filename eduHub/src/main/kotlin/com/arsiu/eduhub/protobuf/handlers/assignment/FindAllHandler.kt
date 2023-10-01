package com.arsiu.eduhub.protobuf.handlers.assignment

import com.arsiu.eduhub.mapper.AssignmentNatsMapper
import com.arsiu.eduhub.model.Assignment
import com.arsiu.eduhub.service.AssignmentService
import com.arsiu.eduhub.v2.assignmentsvc.input.reqreply.assignment.FindAllAssignmentRequest
import com.arsiu.eduhub.v2.assignmentsvc.output.reqreply.assignment.FindAllAssignmentResponse
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class FindAllHandler(
    private val service: AssignmentService,
    private val mapper: AssignmentNatsMapper
) : AssignmentHandler<FindAllAssignmentRequest, FindAllAssignmentResponse>() {

    @Suppress("UnusedParameter")
    fun handleFindAll(request: Mono<FindAllAssignmentRequest>): Mono<FindAllAssignmentResponse> =
        findAll()

    fun findAll(): Mono<FindAllAssignmentResponse> =
        service.findAll().collectList().map {
            successFindAllResponse(it)
        }.doOnError {
            failureResponse(it.javaClass.simpleName, it.message ?: "Unknown error")
        }

    fun successFindAllResponse(assignments: List<Assignment>): FindAllAssignmentResponse =
        FindAllAssignmentResponse.newBuilder().apply {
            responseBuilder.successBuilder
                .setMessage("Assignments returned successfully")
                .assignmentsBuilder
                .addAllAssignment(mapper.toResponseDtoList(assignments))
        }.build()
}
