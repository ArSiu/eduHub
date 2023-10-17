package com.arsiu.eduhub.course.infrastructure.adapters.redis.config

import com.arsiu.eduhub.common.infrastructure.config.redis.BaseRedisConfig
import com.arsiu.eduhub.course.infrastructure.persistence.entity.MongoCourse
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.core.ReactiveRedisTemplate

@Configuration
@EnableCaching
class CourseRedisConfiguration : BaseRedisConfig() {

    @Bean
    fun courseReactiveRedisTemplate(): ReactiveRedisTemplate<String, MongoCourse> {
        return createReactiveRedisTemplate(MongoCourse::class.java)
    }

}
