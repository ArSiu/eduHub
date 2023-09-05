package com.arsiu.eduhub.repository

import com.arsiu.eduhub.model.Assignment
import com.arsiu.eduhub.repository.custom.AssignmentCustomRepository
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface AssignmentRepository : MongoRepository<Assignment, String>, AssignmentCustomRepository
