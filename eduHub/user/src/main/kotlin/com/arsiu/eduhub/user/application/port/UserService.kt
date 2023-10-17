package com.arsiu.eduhub.user.application.port

import com.arsiu.eduhub.common.application.port.GeneralService
import com.arsiu.eduhub.user.domain.User

interface UserService : GeneralService<User, String>
