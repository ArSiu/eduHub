package com.arsiu.eduhub.chapter.application.ports

import com.arsiu.eduhub.chapter.domain.Chapter
import com.arsiu.eduhub.common.application.ports.repository.GeneralRepository
import org.springframework.stereotype.Repository

@Repository
interface ChapterRepository : GeneralRepository<Chapter, String>
