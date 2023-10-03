package com.arsiu.eduhub.protobuf.handlers.assignment

import com.arsiu.eduhub.mapper.AssignmentNatsMapper
import com.arsiu.eduhub.model.Assignment
import com.arsiu.eduhub.service.AssignmentService
import com.arsiu.eduhub.v2.assignmentsvc.input.reqreply.assignment.FindAllAssignmentRequest
import com.arsiu.eduhub.v2.assignmentsvc.output.reqreply.assignment.FindAllAssignmentStreamResponse
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux

@Component
class FindAllStreamHandler(
    private val service: AssignmentService,
    private val mapper: AssignmentNatsMapper
) : AssignmentHandler<FindAllAssignmentRequest, FindAllAssignmentStreamResponse>() {

    fun handleFindAll(request: FindAllAssignmentRequest): Flux<FindAllAssignmentStreamResponse> =
        findAllStream(request)

    @Suppress("UnusedParameter")
    fun findAllStream(request: FindAllAssignmentRequest): Flux<FindAllAssignmentStreamResponse> =
        service.findAll()
            .map { assignment -> successFindAllStreamResponse(assignment) }
            .doOnError { ex ->
                failureResponse(ex.javaClass.simpleName, ex.message ?: "Unknown error")
            }

    fun successFindAllStreamResponse(assignment: Assignment): FindAllAssignmentStreamResponse =
        FindAllAssignmentStreamResponse.newBuilder().apply {
            setResponse(mapper.toResponseDto(assignment))
        }.build()

}
