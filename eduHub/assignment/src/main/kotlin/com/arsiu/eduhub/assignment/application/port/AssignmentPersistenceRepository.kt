package com.arsiu.eduhub.assignment.application.port

import com.arsiu.eduhub.assignment.domain.Assignment
import com.arsiu.eduhub.common.application.port.GeneralPersistenceRepository

interface AssignmentPersistenceRepository : GeneralPersistenceRepository<Assignment, String>
