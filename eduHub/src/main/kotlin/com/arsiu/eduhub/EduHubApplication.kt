package com.arsiu.eduhub

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories

@SpringBootApplication
@EnableReactiveMongoRepositories
@OpenAPIDefinition(info = Info(title = "APIs", version = "1.0", description = "Documentation APIs v1.0"))
class EduHubApplication

@Suppress("SpreadOperator")
fun main(args: Array<String>) {
    runApplication<EduHubApplication>(*args)
}
