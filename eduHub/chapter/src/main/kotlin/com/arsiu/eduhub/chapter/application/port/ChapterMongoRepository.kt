package com.arsiu.eduhub.chapter.application.port

import com.arsiu.eduhub.chapter.domain.Chapter
import com.arsiu.eduhub.common.application.port.repository.mongo.GeneralMongoRepository
import org.springframework.stereotype.Repository

@Repository
interface ChapterMongoRepository : GeneralMongoRepository<Chapter, String>
