package com.arsiu.eduhub.grcp

import com.arsiu.eduhub.base.BaseAssignmentTest
import com.arsiu.eduhub.model.Assignment
import com.arsiu.eduhub.protobuf.handlers.assignment.AssignmentHandler
import com.arsiu.eduhub.testcontainers.TestContainers
import com.arsiu.eduhub.v2.assignmentsvc.ReactorAssignmentServiceGrpc
import com.arsiu.eduhub.v2.assignmentsvc.commonmodels.assignment.AssignmentRequest
import com.arsiu.eduhub.v2.assignmentsvc.commonmodels.assignment.AssignmentResponse
import com.arsiu.eduhub.v2.assignmentsvc.input.reqreply.assignment.DeleteByIdAssignmentRequest
import com.arsiu.eduhub.v2.assignmentsvc.input.reqreply.assignment.FindAllAssignmentRequest
import com.arsiu.eduhub.v2.assignmentsvc.input.reqreply.assignment.FindByIdAssignmentRequest
import com.arsiu.eduhub.v2.assignmentsvc.input.reqreply.assignment.UpdateAssignmentRequest
import com.arsiu.eduhub.v2.assignmentsvc.output.reqreply.assignment.DeleteByIdAssignmentResponse
import com.arsiu.eduhub.v2.assignmentsvc.output.reqreply.assignment.FindAllAssignmentStreamResponse
import com.arsiu.eduhub.v2.assignmentsvc.output.reqreply.assignment.FindByIdAssignmentResponse
import com.arsiu.eduhub.v2.assignmentsvc.output.reqreply.assignment.UpdateAssignmentResponse
import io.grpc.testing.GrpcCleanupRule
import net.devh.boot.grpc.client.inject.GrpcClient
import org.junit.Rule
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import reactor.core.publisher.Flux
import reactor.test.StepVerifier

@SpringBootTest(
    properties = [
        "grpc.server.inProcessName=test",
        "grpc.server.port=-1",
        "grpc.client.inProcess.address=in-process:test"
    ]
)
@ExtendWith(TestContainers::class)
class AssignmentGrpcServiceTest : BaseAssignmentTest() {

    @get:Rule
    val grpcCleanup = GrpcCleanupRule()

    @GrpcClient("inProcess")
    lateinit var grpcService: ReactorAssignmentServiceGrpc.ReactorAssignmentServiceStub

    @Test
    fun `test find all assignments`() {
        val (expected, message) = createExpectedAndMessageForFindAll()
        val response = grpcService.findAll(message)

        val zipped = Flux.zip(expected, response)
        StepVerifier.create(zipped)
            .consumeNextWith { Assertions.assertEquals(it.t1, it.t2) }
            .expectComplete()
            .verify()
    }

    @Test
    fun `test find assignment by ID`() {
        val (expected, message) = createExpectedAndMessageForFindById()
        val response = grpcService.findById(message)

        StepVerifier.create(response)
            .expectNextMatches { actualResponse -> expected == actualResponse }
            .verifyComplete()
    }

    @Test
    fun `test update assignment by ID`() {
        val (expected, message) = createExpectedAndMessageForUpdate()
        val response = grpcService.update(message)

        StepVerifier.create(response)
            .expectNextMatches { actualResponse -> expected == actualResponse }
            .verifyComplete()
    }

    @Test
    fun `test delete assignment by ID`() {
        val (expected, message) = createExpectedAndMessageForDelete()
        val response = grpcService.deleteById(message)

        StepVerifier.create(response)
            .expectNextMatches { actualResponse -> expected == actualResponse }
            .verifyComplete()
    }

    private fun createExpectedAndMessageForFindAll():
            Pair<Flux<FindAllAssignmentStreamResponse>, FindAllAssignmentRequest> {
        val expected = service.findAll().map {
            FindAllAssignmentStreamResponse.newBuilder().apply {
                setResponse(mapper.toResponseDto(it))
            }.build()
        }
        val message = FindAllAssignmentRequest.newBuilder().build()

        return Pair(expected, message)
    }

    private fun createExpectedAndMessageForFindById(): Pair<FindByIdAssignmentResponse, FindByIdAssignmentRequest> {
        val assignment = Assignment().apply {
            id = assignmentId
            name = assignmentName
        }

        val expected = findByIdHandler.successFindByIdResponse(assignment)

        val message = FindByIdAssignmentRequest.newBuilder().apply {
            requestBuilder.assignmentIdBuilder.setId(assignmentId)
        }.build()

        return Pair(expected, message)
    }

    private fun createExpectedAndMessageForUpdate(): Pair<UpdateAssignmentResponse, UpdateAssignmentRequest> {
        val expectedAssignment = Assignment().apply {
            id = assignmentId
            name = "test1"
        }

        val expected = updateHandler.successUpdateResponse(expectedAssignment)

        val message = UpdateAssignmentRequest.newBuilder().apply {
            requestBuilder.setAssignment(mapper.toResponseDto(expectedAssignment))
        }.build()

        return Pair(expected, message)
    }

    private fun createExpectedAndMessageForDelete(): Pair<DeleteByIdAssignmentResponse, DeleteByIdAssignmentRequest> {
        val expected = deleteByIdHandler.successDeleteResponse()

        val message = DeleteByIdAssignmentRequest.newBuilder().apply {
            requestBuilder.assignmentIdBuilder.setId(assignmentId)
        }.build()

        return Pair(expected, message)
    }
}
