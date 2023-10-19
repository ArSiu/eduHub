package com.arsiu.eduhub.it.redis

import com.arsiu.eduhub.course.domain.Course
import com.arsiu.eduhub.it.base.BaseCourseTest
import com.arsiu.eduhub.it.testcontainers.TestContainers
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import reactor.test.StepVerifier

@SpringBootTest
@ExtendWith(TestContainers::class)
class CourseRedisRepositoryTest : BaseCourseTest() {

    private val course1 = getTestCourse()
    private val course2 = getTestCourse().apply { id = "6528ffd26949bd51bd1f6f13" }

    @BeforeEach
    fun setup() {
        courseRedisRepository.deleteById(course1.id).block()
        courseRedisRepository.deleteById(course2.id).block()
    }

    @Test
    fun `should save and retrieve course`() {
        StepVerifier.create(
            courseRedisRepository.save(course1)
                .then(courseRedisRepository.findById(course1.id))
        )
            .assertNext { course ->
                Assertions.assertEquals(course1, course)
            }
            .verifyComplete()
    }

    @Test
    fun `should find all courses`() {
        val savedCourses = mutableListOf<Course>()

        StepVerifier.create(
            courseRedisRepository.save(course1)
                .doOnNext { savedCourses.add(it) }
                .then(courseRedisRepository.save(course2))
                .doOnNext { savedCourses.add(it) }
                .thenMany(courseRedisRepository.findAll())
        )
            .recordWith { ArrayList() }
            .expectNextCount(2)
            .consumeRecordedWith { retrievedCourses ->
                Assertions.assertTrue(savedCourses.containsAll(retrievedCourses))
            }
            .verifyComplete()
    }

    @Test
    fun `should delete course by id`() {
        StepVerifier.create(
            courseRedisRepository.save(course1)
                .then(courseRedisRepository.deleteById(course1.id))
                .then(courseRedisRepository.findById(course1.id))
        )
            .expectComplete()
            .verify()
    }

}
