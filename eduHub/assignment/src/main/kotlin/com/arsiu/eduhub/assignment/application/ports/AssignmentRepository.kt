package com.arsiu.eduhub.assignment.application.ports

import com.arsiu.eduhub.assignment.domain.Assignment
import com.arsiu.eduhub.common.application.ports.repository.GeneralRepository

interface AssignmentRepository : GeneralRepository<Assignment, String>
