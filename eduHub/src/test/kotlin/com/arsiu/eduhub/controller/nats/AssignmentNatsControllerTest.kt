package com.arsiu.eduhub.controller.nats

import com.arsiu.eduhub.mapper.AssignmentNatsMapper
import com.arsiu.eduhub.model.Assignment
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
    private lateinit var natsConnection: Connection

    @Autowired
    private lateinit var mapper: AssignmentNatsMapper

    @BeforeEach
    fun beforeEach() {
        mongoTemplate.save(Assignment().apply {
            id = "test"
            name = "test"
        })
    }

    @Test
    fun testFindById() {
        val expected = FindByIdAssignmentResponse.newBuilder().apply {
            responseBuilder.successBuilder
                .setMessage("Assignment find successfully")
                .assignmentBuilder
                .setId("test")
                .setName("test")
        }.build()

        val message = FindByIdAssignmentRequest.newBuilder().apply {
            requestBuilder.assignmentIdBuilder.setId("test")
        }.build()

        val future = natsConnection.request(ASSIGNMENT_BY_ID, message.toByteArray())
        val result = FindByIdAssignmentResponse.parseFrom(future.get().data)

        Assertions.assertEquals(expected, result)
    }

    @Test
    fun deleteById() {
        val expected = DeleteByIdAssignmentResponse.newBuilder().apply {
            responseBuilder.successBuilder
                .setMessage("Assignment deleted successfully")
        }.build()

        val message = DeleteByIdAssignmentRequest.newBuilder().apply {
            requestBuilder.assignmentIdBuilder.setId("test")
        }.build()

        val future = natsConnection.request(ASSIGNMENT_DELETE_BY_ID, message.toByteArray())
        val result = DeleteByIdAssignmentResponse.parseFrom(future.get().data)

        Assertions.assertEquals(expected, result)
    }

    @Test
    fun findAll() {
        val expectedAssignments = mapper.toResponseDtoList(mongoTemplate.findAll(Assignment::class.java))

        val expected = FindAllAssignmentResponse.newBuilder().apply {
            responseBuilder.successBuilder
                .setMessage("Assignments returned successfully")
                .assignmentsBuilder
                .addAllAssignments(expectedAssignments)
        }.build()

        val message = FindAllAssignmentRequest.newBuilder().build()

        val future = natsConnection.request(ASSIGNMENT_FIND_ALL, message.toByteArray())
        val result = FindAllAssignmentResponse.parseFrom(future.get().data)

        Assertions.assertEquals(expected, result)
    }

    @Test
    fun updateById() {
        val expectedAssignment = Assignment().apply {
            id = "test"
            name = "test1"
        }

        val expected = UpdateAssignmentResponse.newBuilder().apply {
            responseBuilder.successBuilder
                .setMessage("Assignment updated successfully")
                .setAssignment(mapper.toResponseDto(expectedAssignment))
        }.build()

        val message = UpdateAssignmentRequest.newBuilder().apply {
            requestBuilder.setAssignment(mapper.toResponseDto(expectedAssignment))
        }.build()

        val future = natsConnection.request(ASSIGNMENT_UPDATE_BY_ID, message.toByteArray())
        val result = UpdateAssignmentResponse.parseFrom(future.get().data)

        Assertions.assertEquals(expected, result)
    }

}
