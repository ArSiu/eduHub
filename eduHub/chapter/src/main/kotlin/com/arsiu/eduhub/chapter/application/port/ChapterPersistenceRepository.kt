package com.arsiu.eduhub.chapter.application.port

import com.arsiu.eduhub.chapter.domain.Chapter
import com.arsiu.eduhub.common.application.port.GeneralPersistenceRepository

interface ChapterPersistenceRepository : GeneralPersistenceRepository<Chapter, String>
