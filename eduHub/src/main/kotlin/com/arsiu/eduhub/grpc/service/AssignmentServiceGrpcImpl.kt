package com.arsiu.eduhub.grpc.service

import com.arsiu.eduhub.protobuf.handlers.assignment.DeleteByIdHandler
import com.arsiu.eduhub.protobuf.handlers.assignment.FindAllStreamHandler
import com.arsiu.eduhub.protobuf.handlers.assignment.FindByIdHandler
import com.arsiu.eduhub.protobuf.handlers.assignment.UpdateHandler
import com.arsiu.eduhub.v2.assignmentsvc.ReactorAssignmentServiceGrpc.AssignmentServiceImplBase
import com.arsiu.eduhub.v2.assignmentsvc.input.reqreply.assignment.DeleteByIdAssignmentRequest
import com.arsiu.eduhub.v2.assignmentsvc.input.reqreply.assignment.FindAllAssignmentRequest
import com.arsiu.eduhub.v2.assignmentsvc.input.reqreply.assignment.FindByIdAssignmentRequest
import com.arsiu.eduhub.v2.assignmentsvc.input.reqreply.assignment.UpdateAssignmentRequest
import com.arsiu.eduhub.v2.assignmentsvc.output.reqreply.assignment.DeleteByIdAssignmentResponse
import com.arsiu.eduhub.v2.assignmentsvc.output.reqreply.assignment.FindAllAssignmentStreamResponse
import com.arsiu.eduhub.v2.assignmentsvc.output.reqreply.assignment.FindByIdAssignmentResponse
import com.arsiu.eduhub.v2.assignmentsvc.output.reqreply.assignment.UpdateAssignmentResponse
import net.devh.boot.grpc.server.service.GrpcService
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@GrpcService
class AssignmentServiceGrpcImpl(
    private val findAllStreamHandler: FindAllStreamHandler,
    private val findByIdHandler: FindByIdHandler,
    private val updateHandler: UpdateHandler,
    private val deleteByIdHandler: DeleteByIdHandler
) : AssignmentServiceImplBase() {

    override fun findAll(request: Mono<FindAllAssignmentRequest>): Flux<FindAllAssignmentStreamResponse> =
        findAllStreamHandler.handleFindAll(request)

    override fun findById(request: Mono<FindByIdAssignmentRequest>): Mono<FindByIdAssignmentResponse> =
        findByIdHandler.handleFindById(request)

    override fun update(request: Mono<UpdateAssignmentRequest>): Mono<UpdateAssignmentResponse> =
        updateHandler.handleUpdate(request)

    override fun deleteById(request: Mono<DeleteByIdAssignmentRequest>): Mono<DeleteByIdAssignmentResponse> =
        deleteByIdHandler.handleDelete(request)

}
