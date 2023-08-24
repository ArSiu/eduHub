package com.arsiu.eduhub.repository

import com.arsiu.eduhub.model.Assignment
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface AssignmentRepository : CrudRepository<Assignment, Long>
