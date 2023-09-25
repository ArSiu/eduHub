package com.arsiu.eduhub.repository

import com.arsiu.eduhub.model.Chapter
import org.springframework.stereotype.Repository

@Repository
interface ChapterRepository : CascadeRepository<Chapter, String>
