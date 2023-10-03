package com.arsiu.eduhub.it.grcp

import com.arsiu.eduhub.it.base.BaseAssignmentTest
import com.arsiu.eduhub.it.testcontainers.TestContainers
import com.arsiu.eduhub.model.Assignment
import com.arsiu.eduhub.v2.assignmentsvc.ReactorAssignmentServiceGrpc
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
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import reactor.core.publisher.Flux
import reactor.test.StepVerifier
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

@SpringBootTest(
    properties = [
        "grpc.server.inProcessName=test",
        "grpc.server.port=9091",
        "grpc.client.inProcess.address=in-process:test"
    ]
)
@ExtendWith(TestContainers::class)
@TestMethodOrder(OrderAnnotation::class)
class AssignmentGrpcServiceTest : BaseAssignmentTest() {

    @get:Rule
    val grpcCleanup = GrpcCleanupRule()

    @GrpcClient("inProcess")
    lateinit var grpcService: ReactorAssignmentServiceGrpc.ReactorAssignmentServiceStub

    @Test
    @Order(2)
    fun `test find all assignments stream`() {
        val latch = CountDownLatch(2)
        val collectedItems = CopyOnWriteArrayList<FindAllAssignmentStreamResponse>()

        val subscription = grpcService.findAll(FindAllAssignmentRequest.getDefaultInstance()).subscribe(
            {
                collectedItems.add(it)
                latch.countDown()
            },
            { latch.countDown() }
        )

        latch.await(5, TimeUnit.SECONDS)
        val (expected, message) = createExpectedAndMessageForUpdate()
        grpcService.update(message).subscribe()

        latch.await(5, TimeUnit.SECONDS)

        Assertions.assertEquals(2, collectedItems.size)

        subscription.dispose()
    }

    @Test
    @Order(3)
    fun `test find all assignments`() {
        val (expected, message) = createExpectedAndMessageForFindAll()
        val response = grpcService.findAll(message)

        val zipped = Flux.zip(expected, response)
        StepVerifier.create(zipped)
            .assertNext { Assertions.assertEquals(it.t1, it.t2) }
            .expectComplete()
            .verify()
    }

    @Test
    @Order(4)
    fun `test find assignment by ID`() {
        val (expected, message) = createExpectedAndMessageForFindById()
        val response = grpcService.findById(message)

        StepVerifier.create(response)
            .assertNext { actualResponse -> Assertions.assertEquals(expected, actualResponse) }
            .verifyComplete()
    }

    @Test
    @Order(5)
    fun `test update assignment by ID`() {
        val (expected, message) = createExpectedAndMessageForUpdate()
        val response = grpcService.update(message)

        StepVerifier.create(response)
            .assertNext { actualResponse -> Assertions.assertEquals(expected, actualResponse) }
            .verifyComplete()
    }

    @Test
    @Order(6)
    fun `test delete assignment by ID`() {
        val (expected, message) = createExpectedAndMessageForDelete()
        val response = grpcService.deleteById(message)

        StepVerifier.create(response)
            .assertNext { actualResponse -> Assertions.assertEquals(expected, actualResponse) }
            .verifyComplete()
    }

    private fun createExpectedAndMessageForFindAll():
            Pair<Flux<FindAllAssignmentStreamResponse>, FindAllAssignmentRequest> {
        val expected = service.findAll().map {
            FindAllAssignmentStreamResponse.newBuilder().apply {
                setResponse(mapper.toResponseDto(it))
            }.build()
        }
        val message = FindAllAssignmentRequest.getDefaultInstance()

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
