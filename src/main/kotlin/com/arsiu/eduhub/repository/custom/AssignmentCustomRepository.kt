package com.arsiu.eduhub.repository.custom

import com.arsiu.eduhub.model.Assignment

interface AssignmentCustomRepository : CascadeRepository<Assignment, String>
