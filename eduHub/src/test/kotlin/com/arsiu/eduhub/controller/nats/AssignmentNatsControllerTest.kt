package com.arsiu.eduhub.controller.nats

import com.arsiu.eduhub.mapper.AssignmentNatsMapper
import com.arsiu.eduhub.model.Assignment
import com.arsiu.eduhub.v2.assignmentsvc.NatsSubject.ASSIGNMENT_BY_ID
import com.arsiu.eduhub.v2.assignmentsvc.NatsSubject.ASSIGNMENT_DELETE_BY_ID
import com.arsiu.eduhub.v2.assignmentsvc.NatsSubject.ASSIGNMENT_FIND_ALL
import com.arsiu.eduhub.v2.assignmentsvc.NatsSubject.ASSIGNMENT_UPDATE_BY_ID
import com.arsiu.eduhub.v2.assignmentsvc.commonmodels.assignment.AssignmentProto
import com.arsiu.eduhub.v2.assignmentsvc.commonmodels.assignment.AssignmentProto.AssignmentId
import com.arsiu.eduhub.v2.assignmentsvc.commonmodels.assignment.AssignmentProto.AssignmentRequest
import com.arsiu.eduhub.v2.assignmentsvc.commonmodels.assignment.AssignmentProto.AssignmentResponse
import com.arsiu.eduhub.v2.assignmentsvc.commonmodels.assignment.AssignmentProto.AssignmentResponse.Success
import com.arsiu.eduhub.v2.assignmentsvc.input.reqreply.assignment.DeleteByIdAssignmentRequestProto.DeleteByIdAssignmentRequest
import com.arsiu.eduhub.v2.assignmentsvc.input.reqreply.assignment.FindAllAssignmentRequestProto.FindAllAssignmentRequest
import com.arsiu.eduhub.v2.assignmentsvc.input.reqreply.assignment.FindByIdAssignmentRequestProto.FindByIdAssignmentRequest
import com.arsiu.eduhub.v2.assignmentsvc.input.reqreply.assignment.UpdateAssignmentRequestProto.UpdateAssignmentRequest
import com.arsiu.eduhub.v2.assignmentsvc.output.reqreply.assignment.DeleteByIdAssignmentResponseProto.DeleteByIdAssignmentResponse
import com.arsiu.eduhub.v2.assignmentsvc.output.reqreply.assignment.FindAllAssignmentResponseProto.FindAllAssignmentResponse
import com.arsiu.eduhub.v2.assignmentsvc.output.reqreply.assignment.FindByIdAssignmentResponseProto.FindByIdAssignmentResponse
import com.arsiu.eduhub.v2.assignmentsvc.output.reqreply.assignment.UpdateAssignmentResponseProto.UpdateAssignmentResponse
import io.nats.client.Connection
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.mongodb.core.MongoTemplate

@SpringBootTest
@ExtendWith(TestContainers::class)
class AssignmentNatsControllerTest {

    @Autowired
    private lateinit var mongoTemplate: MongoTemplate

    @Autowired
    private lateinit var natsConnection:Connection

    @Autowired
    private lateinit var mapper: AssignmentNatsMapper

    @BeforeEach
    fun beforeEach(){
        mongoTemplate.save(Assignment().apply {
            id = "test"
            name = "test"
        })
    }

    @Test
    fun testFindById() {
        val expectedAssignment = AssignmentProto.Assignment.newBuilder()
            .setId("test")
            .setName("test")
            .build()
        val expectedSuccess = Success.newBuilder()
            .setMessage("Assignment find successfully")
            .setAssignment(expectedAssignment)
            .build()
        val expectedResponse = AssignmentResponse.newBuilder()
            .setSuccess(expectedSuccess)
            .build()
        val expected = FindByIdAssignmentResponse.newBuilder()
            .setResponse(expectedResponse)
            .build()

        val assignmentId = AssignmentId.newBuilder()
            .setId("test")
            .build()
        val request = AssignmentRequest.newBuilder()
            .setAssignmentId(assignmentId)
            .build()
        val message = FindByIdAssignmentRequest.newBuilder()
            .setRequest(request)
            .build()

        val future = natsConnection.request(ASSIGNMENT_BY_ID, message.toByteArray())
        val response = future.get()
        val result = FindByIdAssignmentResponse.parseFrom(response.data)

        Assertions.assertEquals(expected, result)
    }

    @Test
    fun deleteById() {
        val expectedSuccess = Success.newBuilder()
            .setMessage("Assignment deleted successfully")
            .build()
        val expectedResponse = AssignmentResponse.newBuilder()
            .setSuccess(expectedSuccess)
            .build()
        val expected = DeleteByIdAssignmentResponse.newBuilder()
            .setResponse(expectedResponse)
            .build()

        val assignmentId = AssignmentId.newBuilder()
            .setId("test")
            .build()
        val request = AssignmentRequest.newBuilder()
            .setAssignmentId(assignmentId)
            .build()
        val message = DeleteByIdAssignmentRequest.newBuilder()
            .setRequest(request)
            .build()

        val future = natsConnection.request(ASSIGNMENT_DELETE_BY_ID, message.toByteArray())
        val response = future.get()
        val result = DeleteByIdAssignmentResponse.parseFrom(response.data)

        Assertions.assertEquals(expected, result)
    }

    @Test
    fun findAll() {
        val expectedAssignments = mapper.toResponseDtoList(mongoTemplate.findAll(Assignment::class.java))
        val expectedSuccess: Success = Success.newBuilder()
            .setMessage("Assignments returned successfully")
            .setAssignments(AssignmentProto.Assignments.newBuilder().addAllAssignments(expectedAssignments))
            .build()
        val expectedResponse = AssignmentResponse.newBuilder()
            .setSuccess(expectedSuccess)
            .build()
        val expected =  FindAllAssignmentResponse.newBuilder()
            .setResponse(expectedResponse)
            .build()

        val message = FindAllAssignmentRequest.newBuilder()
            .build()

        val future = natsConnection.request(ASSIGNMENT_FIND_ALL, message.toByteArray())
        val response = future.get()
        val result = FindAllAssignmentResponse.parseFrom(response.data)

        Assertions.assertEquals(expected, result)
    }

    @Test
    fun updateById() {
        val expectedAssignment = Assignment().apply {
            id = "test"
            name = "test1"
        }
        val expectedSuccess: Success = Success.newBuilder()
            .setMessage("Assignment updated successfully")
            .setAssignment(mapper.toResponseDto(expectedAssignment))
            .build()
        val expectedResponse = AssignmentResponse.newBuilder()
            .setSuccess(expectedSuccess)
            .build()
        val expected = UpdateAssignmentResponse.newBuilder()
            .setResponse(expectedResponse)
            .build()

        val request = AssignmentRequest.newBuilder().setAssignment(mapper.toResponseDto(expectedAssignment))
        val message = UpdateAssignmentRequest.newBuilder()
            .setRequest(request)
            .build()

        val future = natsConnection.request(ASSIGNMENT_UPDATE_BY_ID, message.toByteArray())
        val response = future.get()
        val result = UpdateAssignmentResponse.parseFrom(response.data)

        Assertions.assertEquals(expected, result)
    }


}
