package com.arsiu.eduhub.user.application.ports

import com.arsiu.eduhub.common.application.ports.service.GeneralService
import com.arsiu.eduhub.user.domain.User

interface UserService : GeneralService<User, String>
