package com.arsiu.eduhub.repository

import com.arsiu.eduhub.model.Assignment

interface AssignmentRepository : CascadeRepository<Assignment, String>
