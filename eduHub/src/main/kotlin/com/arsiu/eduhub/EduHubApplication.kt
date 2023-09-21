package com.arsiu.eduhub

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories

@SpringBootApplication
@EnableReactiveMongoRepositories
class EduHubApplication

@Suppress("SpreadOperator")
fun main(args: Array<String>) {
    runApplication<EduHubApplication>(*args)
}
