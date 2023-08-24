package com.arsiu.eduhub.repository

import com.arsiu.eduhub.model.Chapter
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ChapterRepository : CrudRepository<Chapter, Long>
