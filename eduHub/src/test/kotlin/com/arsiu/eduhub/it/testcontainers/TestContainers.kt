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
                withExposedService("redis", 6379)
                withExposedService("zookeeper", 2181)
                withExposedService("kafka", 9092)
                withExposedService("schema-registry", 8081)
            }

    }

    override fun beforeAll(context: ExtensionContext?) {
        environment.start()

        // MongoDB settings
        setMongoDbProperties("root", "root", "myDb")

        // NATS settings
        setNatsProperties("nats", 4222)

        // Kafka settings
        setKafkaProperties("localhost", "kafka", 9092, "schema-registry", 8081)

        // Redis settings
        setRedisProperties("localhost", "redis", 6379)

        environment.waitingFor(
            "kafka",
            Wait.forLogMessage(".*Assignment received from.*\\s", 1)
        )
    }

    private fun setMongoDbProperties(username: String, password: String, database: String) {
        System.setProperty("spring.data.mongodb.username", username)
        System.setProperty("spring.data.mongodb.password", password)
        System.setProperty("spring.data.mongodb.database", database)
        System.setProperty(
            "spring.data.mongodb.host",
            environment.getServiceHost("mongodb", 27017)
        )
        System.setProperty(
            "spring.data.mongodb.port",
            environment.getServicePort("mongodb", 27017).toString()
        )
    }

    private fun setNatsProperties(serviceName: String, port: Int) {
        System.setProperty(
            "nats.url",
            "nats://${
                environment.getServiceHost(serviceName, port)
            }:${
                environment.getServicePort(serviceName, port)
            }"
        )
    }

    private fun setKafkaProperties(
        host: String,
        kafkaServiceName: String,
        kafkaPort: Int,
        registryServiceName: String,
        registryPort: Int
    ) {
        System.setProperty(
            "spring.kafka.bootstrap-servers",
            "$host:${environment.getServicePort(kafkaServiceName, kafkaPort)}"
        )
        System.setProperty(
            "spring.kafka.properties.schema.registry.url",
            "http://$host:${environment.getServicePort(registryServiceName, registryPort)}"
        )
    }

    private fun setRedisProperties(host: String, serviceName: String, port: Int) {
        System.setProperty("spring.data.redis.host", host)
        System.setProperty("spring.data.redis.port", "${
            environment.getServicePort(serviceName, port)
        }")
    }

}
