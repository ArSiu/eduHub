package com.arsiu.eduhub.user.application.port

import com.arsiu.eduhub.common.application.port.repository.GeneralRepository
import com.arsiu.eduhub.user.domain.User

interface UserRepository : GeneralRepository<User, String>
