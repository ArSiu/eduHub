package com.arsiu.eduhub.it.nats.controller

import com.arsiu.eduhub.it.base.BaseAssignmentTest
import com.arsiu.eduhub.it.testcontainers.TestContainers
import com.arsiu.eduhub.assignment.domain.Assignment
import com.arsiu.eduhub.v2.assignmentsvc.NatsSubject.ASSIGNMENT_BY_ID
import com.arsiu.eduhub.v2.assignmentsvc.NatsSubject.ASSIGNMENT_DELETE_BY_ID
import com.arsiu.eduhub.v2.assignmentsvc.NatsSubject.ASSIGNMENT_FIND_ALL
import com.arsiu.eduhub.v2.assignmentsvc.NatsSubject.ASSIGNMENT_UPDATE_BY_ID
import com.arsiu.eduhub.v2.assignmentsvc.input.reqreply.assignment.DeleteByIdAssignmentRequest
import com.arsiu.eduhub.v2.assignmentsvc.input.reqreply.assignment.FindAllAssignmentRequest
import com.arsiu.eduhub.v2.assignmentsvc.input.reqreply.assignment.FindByIdAssignmentRequest
import com.arsiu.eduhub.v2.assignmentsvc.input.reqreply.assignment.UpdateAssignmentRequest
import com.arsiu.eduhub.v2.assignmentsvc.output.reqreply.assignment.DeleteByIdAssignmentResponse
import com.arsiu.eduhub.v2.assignmentsvc.output.reqreply.assignment.FindAllAssignmentResponse
import com.arsiu.eduhub.v2.assignmentsvc.output.reqreply.assignment.FindByIdAssignmentResponse
import com.arsiu.eduhub.v2.assignmentsvc.output.reqreply.assignment.UpdateAssignmentResponse
import io.nats.client.Connection
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
@ExtendWith(TestContainers::class)

class AssignmentNatsControllerTest : BaseAssignmentTest() {

    @Autowired
    private lateinit var natsConnection: Connection

    @Test
    fun `test find all assignments`() {
        val (expected, message) = createExpectedAndMessageForFindAll()

        val result = sendRequest<FindAllAssignmentResponse>(ASSIGNMENT_FIND_ALL, message)

        Assertions.assertEquals(expected, result)
    }

    @Test
    fun `test find assignment by id`() {
        val (expected, message) = createExpectedAndMessageForFindById()

        val result = sendRequest<FindByIdAssignmentResponse>(ASSIGNMENT_BY_ID, message)

        Assertions.assertEquals(expected, result)
    }

    @Test
    fun `test update assignment by id`() {
        val (expected, message) = createExpectedAndMessageForUpdate()

        val result = sendRequest<UpdateAssignmentResponse>(ASSIGNMENT_UPDATE_BY_ID, message)

        Assertions.assertEquals(expected, result)
    }

    @Test
    fun `test delete assignment by id`() {
        val (expected, message) = createExpectedAndMessageForDelete()

        val result = sendRequest<DeleteByIdAssignmentResponse>(ASSIGNMENT_DELETE_BY_ID, message)

        Assertions.assertEquals(expected, result)
    }

    private fun createExpectedAndMessageForFindAll(): Pair<FindAllAssignmentResponse?, FindAllAssignmentRequest> {
        val expected = service.findAll().collectList().block()?.let { findAllHandler.successFindAllResponse(it) }

        val message = FindAllAssignmentRequest.getDefaultInstance()

        return Pair(expected, message)
    }

    private fun createExpectedAndMessageForFindById(): Pair<FindByIdAssignmentResponse?, FindByIdAssignmentRequest> {
        val expected = service.findById(assignmentId).block()?.let { findByIdHandler.successFindByIdResponse(it) }

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

    private inline fun <reified T> sendRequest(subject: String, message: com.google.protobuf.Message): T {
        val future = natsConnection.request(subject, com.google.protobuf.Any.pack(message).toByteArray())
        return T::class.java.getDeclaredMethod("parseFrom", ByteArray::class.java)
            .invoke(null, future.get().data) as T
    }
}
