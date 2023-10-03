package com.arsiu.eduhub.kafka.producer

import com.arsiu.eduhub.v2.assignmentsvc.KafkaTopic.ASSIGNMENT_UPDATE
import com.arsiu.eduhub.v2.assignmentsvc.commonmodels.assignment.AssignmentProto
import org.apache.kafka.clients.producer.ProducerRecord
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import reactor.kafka.sender.KafkaSender
import reactor.kafka.sender.SenderRecord

@Component
class AssignmentKafkaProducer(
    private val assignmentKafkaSender: KafkaSender<String, AssignmentProto>
) {
    private val logger: Logger = LoggerFactory.getLogger(AssignmentKafkaProducer::class.java)

    private val topic = ASSIGNMENT_UPDATE

    fun sendAssignmentUpdateToKafka(assignment: AssignmentProto) {
        val senderRecord = SenderRecord.create(
            ProducerRecord(topic, assignment.id, assignment),
            null
        )
        assignmentKafkaSender.send(Mono.just(senderRecord))
            .doOnError { e ->
                logger.error("Failed to send AssignmentProto with ID ${assignment.id} to topic $topic", e)
            }
            .subscribe()
    }
}
