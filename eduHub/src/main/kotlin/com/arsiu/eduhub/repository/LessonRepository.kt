package com.arsiu.eduhub.repository

import com.arsiu.eduhub.model.Lesson
import com.arsiu.eduhub.repository.custom.LessonCustomRepository
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface LessonRepository : MongoRepository<Lesson, String>, LessonCustomRepository
