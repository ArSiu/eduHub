package com.arsiu.eduhub.course.application.port

import com.arsiu.eduhub.common.application.port.repository.redis.GeneralRedisRepository
import com.arsiu.eduhub.course.domain.Course

interface CourseRedisRepository : GeneralRedisRepository<Course, String>
