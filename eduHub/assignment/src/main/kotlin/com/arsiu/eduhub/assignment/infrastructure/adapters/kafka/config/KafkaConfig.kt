package com.arsiu.eduhub.assignment.infrastructure.adapters.kafka.config

import com.arsiu.eduhub.common.application.config.kafka.BaseKafkaConfig
import com.arsiu.eduhub.v2.assignmentsvc.KafkaTopic.ASSIGNMENT_UPDATE
import com.arsiu.eduhub.v2.assignmentsvc.commonmodels.assignment.AssignmentProto
import io.confluent.kafka.serializers.protobuf.KafkaProtobufDeserializerConfig
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import reactor.kafka.receiver.KafkaReceiver
import reactor.kafka.sender.KafkaSender

@Configuration
class KafkaConfig : BaseKafkaConfig() {

    @Bean
    fun assignmentKafkaReceiver(): KafkaReceiver<String, AssignmentProto> {
        val customProps: MutableMap<String, Any> = mutableMapOf(
            KafkaProtobufDeserializerConfig.SPECIFIC_PROTOBUF_VALUE_TYPE to AssignmentProto::class.java.name
        )
        return createKafkaReceiver(
            baseConsumerProperties(customProps),
            ASSIGNMENT_UPDATE,
            "assignment-group"
        )
    }

    @Bean
    fun assignmentKafkaSender(): KafkaSender<String, AssignmentProto> {
        return createKafkaSender(baseProducerProperties())
    }
}
