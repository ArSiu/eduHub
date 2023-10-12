package com.arsiu.eduhub.it.testcontainers

import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.testcontainers.containers.DockerComposeContainer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.io.File

@Testcontainers
class TestContainers : BeforeAllCallback {

    companion object {

        @Container
        val environment: DockerComposeContainer<*> =
            DockerComposeContainer<Nothing>(File("../docker-compose.yml")).apply {
                withExposedService("mongodb", 27017)
                withExposedService("nats", 4222)
                withExposedService("zookeeper", 2181)
                withExposedService("kafka", 9092)
                withExposedService("schema-registry", 8081)
            }

    }

    override fun beforeAll(context: ExtensionContext?) {
        environment.start()
        System.setProperty("spring.data.mongodb.username", "root")
        System.setProperty("spring.data.mongodb.password", "root")
        System.setProperty("spring.data.mongodb.database", "myDb")
        System.setProperty(
            "spring.data.mongodb.host",
            environment.getServiceHost("mongodb", 27017)
        )
        System.setProperty(
            "spring.data.mongodb.port",
            environment.getServicePort("mongodb", 27017).toString()
        )
        System.setProperty(
            "nats.url", "nats://${environment.getServiceHost("nats", 4222)}:" +
                    "${environment.getServicePort("nats", 4222)}"
        )
        System.setProperty("spring.kafka.bootstrap-servers", "localhost:${environment.getServicePort("kafka", 9092)}")
        System.setProperty(
            "spring.kafka.properties.schema.registry.url",
            "http://localhost:${environment.getServicePort("schema-registry", 8081)}"
        )
        environment.waitingFor("kafka", Wait.forLogMessage(".*Assignment received from.*\\s", 1))
    }

}
