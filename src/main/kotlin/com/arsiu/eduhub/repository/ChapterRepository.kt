package com.arsiu.eduhub.repository

import com.arsiu.eduhub.model.Chapter
import com.arsiu.eduhub.repository.custom.ChapterCustomRepository
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface ChapterRepository : MongoRepository<Chapter, String>, ChapterCustomRepository
