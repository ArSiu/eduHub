package com.arsiu.eduhub.it.base

import com.arsiu.eduhub.mapper.AssignmentNatsMapper
import com.arsiu.eduhub.model.Assignment
import com.arsiu.eduhub.protobuf.handlers.assignment.DeleteByIdHandler
import com.arsiu.eduhub.protobuf.handlers.assignment.FindAllHandler
import com.arsiu.eduhub.protobuf.handlers.assignment.FindAllStreamHandler
import com.arsiu.eduhub.protobuf.handlers.assignment.FindByIdHandler
import com.arsiu.eduhub.protobuf.handlers.assignment.UpdateHandler
import com.arsiu.eduhub.service.AssignmentService
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update

@SpringBootTest
abstract class BaseAssignmentTest {

    @Autowired
    protected lateinit var mongoTemplate: ReactiveMongoTemplate

    @Autowired
    protected lateinit var mapper: AssignmentNatsMapper

    @Autowired
    protected lateinit var service: AssignmentService

    @Autowired
    lateinit var findAllStreamHandler: FindAllStreamHandler

    @Autowired
    lateinit var findAllHandler: FindAllHandler

    @Autowired
    lateinit var findByIdHandler: FindByIdHandler

    @Autowired
    lateinit var updateHandler: UpdateHandler

    @Autowired
    lateinit var deleteByIdHandler: DeleteByIdHandler

    protected val assignmentId = "650e90f20fefff48e6775111"

    protected val assignmentName = "test"

    @BeforeEach
    fun setUpAssignment() {
        val query = Query.query(Criteria.where("id").`is`(assignmentId))

        val update = Update().set("name", assignmentName)

        mongoTemplate.upsert(query, update, Assignment::class.java).block()
    }

}
