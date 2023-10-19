package com.arsiu.eduhub.user.application.port

import com.arsiu.eduhub.common.application.port.GeneralPersistenceRepository
import com.arsiu.eduhub.user.domain.User

interface UserPersistenceRepository : GeneralPersistenceRepository<User, String>
