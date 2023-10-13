package com.arsiu.eduhub.chapter.application.port

import com.arsiu.eduhub.chapter.domain.Chapter
import com.arsiu.eduhub.common.application.port.repository.GeneralRepository
import org.springframework.stereotype.Repository

@Repository
interface ChapterRepository : GeneralRepository<Chapter, String>
