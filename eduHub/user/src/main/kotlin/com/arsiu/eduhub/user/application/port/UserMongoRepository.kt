package com.arsiu.eduhub.user.application.port

import com.arsiu.eduhub.common.application.port.repository.mongo.GeneralMongoRepository
import com.arsiu.eduhub.user.domain.User

interface UserMongoRepository : GeneralMongoRepository<User, String>
