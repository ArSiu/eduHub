package com.arsiu.eduhub.user.application.ports

import com.arsiu.eduhub.common.application.ports.repository.GeneralRepository
import com.arsiu.eduhub.user.domain.User

interface UserRepository : GeneralRepository<User, String>
