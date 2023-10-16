package com.arsiu.eduhub.assignment.infrastructure.adapters.grpc

import com.arsiu.eduhub.assignment.infrastructure.adapters.handlers.assignment.DeleteByIdHandler
import com.arsiu.eduhub.assignment.infrastructure.adapters.handlers.assignment.FindAllStreamHandler
import com.arsiu.eduhub.assignment.infrastructure.adapters.handlers.assignment.FindByIdHandler
import com.arsiu.eduhub.assignment.infrastructure.adapters.handlers.assignment.UpdateHandler
import com.arsiu.eduhub.assignment.infrastructure.adapters.kafka.producer.AssignmentKafkaProducer
import com.arsiu.eduhub.assignment.infrastructure.shared.streams.SharedAssignmentStream
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
    private val deleteByIdHandler: DeleteByIdHandler,
    val sharedAssignmentStream: SharedAssignmentStream,
    val producer: AssignmentKafkaProducer
) : AssignmentServiceImplBase() {

    override fun findAll(request: Mono<FindAllAssignmentRequest>):
            Flux<FindAllAssignmentStreamResponse> =
        Flux.concat(
            request.flatMapMany { findAllStreamHandler.handleFindAll(it) },
            sharedAssignmentStream.flux
        ).log()


    override fun findById(request: Mono<FindByIdAssignmentRequest>):
            Mono<FindByIdAssignmentResponse> =
        request.flatMap { findByIdHandler.handleFindById(it) }

    override fun update(request: Mono<UpdateAssignmentRequest>): Mono<UpdateAssignmentResponse> =
        request.flatMap { updateRequest ->
            updateHandler.handleUpdate(updateRequest)
        }
            .doOnNext { response ->
                producer.sendAssignmentUpdateToKafka(response.response.success.assignment)
            }

    override fun deleteById(request: Mono<DeleteByIdAssignmentRequest>):
            Mono<DeleteByIdAssignmentResponse> =
        request.flatMap { deleteByIdHandler.handleDelete(it) }


}
