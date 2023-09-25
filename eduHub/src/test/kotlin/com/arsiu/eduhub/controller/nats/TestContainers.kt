package com.arsiu.eduhub.controller.nats

import org.junit.jupiter.api.extension.AfterAllCallback
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers
class TestContainers : BeforeAllCallback, AfterAllCallback {

    companion object {

        var mongoContainer: GenericContainer<*> = GenericContainer<Nothing>("mongo:latest")
            .apply {
                withEnv("MONGO_INITDB_ROOT_USERNAME", "root")
                withEnv("MONGO_INITDB_ROOT_PASSWORD", "root")
                withExposedPorts(27017)
                withReuse(true)
            }

        var natsContainer: GenericContainer<*> = GenericContainer<Nothing>("nats:latest")
            .apply {
                withExposedPorts(4222)
                waitingFor(Wait.forListeningPort())
                withReuse(true)
            }
    }

    override fun beforeAll(context: ExtensionContext?) {
        mongoContainer.start()
        natsContainer.start()
        System.setProperty("spring.data.mongodb.username", "root")
        System.setProperty("spring.data.mongodb.password", "root")
        System.setProperty("spring.data.mongodb.database", "myDb")
        System.setProperty("spring.data.mongodb.host", mongoContainer.host)
        System.setProperty("spring.data.mongodb.port", mongoContainer.getMappedPort(27017).toString())
        System.setProperty("nats.url", "nats://${natsContainer.host}:${natsContainer.getMappedPort(4222)}")
    }

    override fun afterAll(context: ExtensionContext?) {
        mongoContainer.stop()
        natsContainer.stop()
    }

}
