package com.arsiu.eduhub.assignment.application.port

import com.arsiu.eduhub.assignment.domain.Assignment
import com.arsiu.eduhub.common.application.port.repository.GeneralRepository

interface AssignmentRepository : GeneralRepository<Assignment, String>
