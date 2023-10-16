package com.arsiu.eduhub.course.infrastructure.persistence.repositories


import com.arsiu.eduhub.course.application.port.CourseRedisRepository
import com.arsiu.eduhub.course.domain.Course
import com.arsiu.eduhub.course.infrastructure.mapper.CourseToEntityMapper
import com.arsiu.eduhub.course.infrastructure.persistence.entity.MongoCourse
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.Duration

@Repository
class CourseRedisRepositoryImpl(
    private val courseReactiveRedisTemplate: ReactiveRedisTemplate<String, MongoCourse>,
    private val mapper: CourseToEntityMapper
) : CourseRedisRepository {

    @Value("\${redis.cache.ttl.minutes}")
    private lateinit var redisTtlMinutes: String

    @Value("\${redis.course.key.prefix}")
    private lateinit var keyCoursePrefix: String

    override fun save(model: Course): Mono<Course> {
        return courseReactiveRedisTemplate
            .opsForValue()
            .set(
                keyCoursePrefix + model.id,
                mapper.toEntityWithId(model),
                Duration.ofMinutes(redisTtlMinutes.toLong())
            )
            .thenReturn(model)
    }

    override fun findById(id: String): Mono<Course> {
        return courseReactiveRedisTemplate
            .opsForValue()
            .get(keyCoursePrefix + id)
            .map {
                mapper.toModelWithChapters(it)
            }
    }

    override fun findAll(): Flux<Course> {
        return courseReactiveRedisTemplate.keys("${keyCoursePrefix}*")
            .flatMap { key ->
                courseReactiveRedisTemplate
                    .opsForValue()
                    .get(key)
                    .map {
                        mapper.toModelWithChapters(it)
                    }
            }
    }

    override fun deleteById(id: String): Mono<Void> {
        return courseReactiveRedisTemplate.opsForValue().delete(keyCoursePrefix + id).then()
    }

}
