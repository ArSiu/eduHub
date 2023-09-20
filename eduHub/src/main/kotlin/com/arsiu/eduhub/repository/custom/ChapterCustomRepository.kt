package com.arsiu.eduhub.repository.custom

import com.arsiu.eduhub.model.Chapter

interface ChapterCustomRepository : CascadeRepository<Chapter, String>
