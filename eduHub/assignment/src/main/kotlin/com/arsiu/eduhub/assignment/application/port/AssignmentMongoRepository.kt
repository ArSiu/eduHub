package com.arsiu.eduhub.assignment.application.port

import com.arsiu.eduhub.assignment.domain.Assignment
import com.arsiu.eduhub.common.application.port.repository.mongo.GeneralMongoRepository

interface AssignmentMongoRepository : GeneralMongoRepository<Assignment, String>
