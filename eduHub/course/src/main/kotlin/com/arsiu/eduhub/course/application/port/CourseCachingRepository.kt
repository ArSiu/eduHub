package com.arsiu.eduhub.course.application.port

import com.arsiu.eduhub.common.application.port.GeneralCachingRepository
import com.arsiu.eduhub.course.domain.Course

interface CourseCachingRepository : GeneralCachingRepository<Course, String>
