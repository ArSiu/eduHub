package com.arsiu.eduhub.common.application.config.redis

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.StringRedisSerializer

@Configuration
@EnableCaching
open class BaseRedisConfig {

    @Value("\${spring.data.redis.host}")
    lateinit var host: String

    @Value("\${spring.data.redis.port}")
    lateinit var port: String

    protected open fun reactiveRedisConnectionFactory(): ReactiveRedisConnectionFactory {
        val config = RedisStandaloneConfiguration(host, port.toInt())
        val factory = LettuceConnectionFactory(config)
        factory.afterPropertiesSet()
        return factory
    }

    protected open fun <T : Any> createReactiveRedisTemplate(
        type: Class<T>
    ): ReactiveRedisTemplate<String, T> {
        val objectMapper = ObjectMapper().findAndRegisterModules()
        val serializer = Jackson2JsonRedisSerializer(objectMapper, type)
        val context = StringRedisSerializer()
        val configuration = RedisSerializationContext
            .newSerializationContext<String, T>(context)
            .value(serializer)
            .build()

        return ReactiveRedisTemplate(reactiveRedisConnectionFactory(), configuration)
    }
}
