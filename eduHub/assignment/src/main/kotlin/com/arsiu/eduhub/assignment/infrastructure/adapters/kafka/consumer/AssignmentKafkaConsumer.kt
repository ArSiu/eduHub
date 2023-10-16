package com.arsiu.eduhub.assignment.infrastructure.adapters.kafka.consumer

import com.arsiu.eduhub.v2.assignmentsvc.NatsSubject
import com.arsiu.eduhub.v2.assignmentsvc.commonmodels.assignment.AssignmentProto
import io.nats.client.Connection
import jakarta.annotation.PostConstruct
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import reactor.kafka.receiver.KafkaReceiver

@Component
class AssignmentKafkaConsumer(
    private val assignmentKafkaReceiver: KafkaReceiver<String, AssignmentProto>,
    private val natsConnection: Connection
) {

    private val logger: Logger = LoggerFactory.getLogger(AssignmentKafkaConsumer::class.java)
    private val subject = NatsSubject.ASSIGNMENT_UPDATE_BUS

    @PostConstruct
    fun listen() {
        assignmentKafkaReceiver.receiveAutoAck()
            .flatMap { fluxRecord ->
                fluxRecord.map { record ->
                    natsConnection.publish(subject, record.value().toByteArray())
                    record
                }
            }.doOnError { e ->
                logger.error("Error while processing Kafka record to publishing on NATS", e)
            }.subscribe()
    }

}
