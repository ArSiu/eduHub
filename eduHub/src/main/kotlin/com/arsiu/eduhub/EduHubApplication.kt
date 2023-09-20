package com.arsiu.eduhub

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class EduHubApplication

@Suppress("SpreadOperator")
fun main(args: Array<String>) {
    runApplication<EduHubApplication>(*args)
}
