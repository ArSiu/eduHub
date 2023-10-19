package com.arsiu.eduhub.common.infrastructure.config.kafka

import com.google.protobuf.GeneratedMessageV3
import io.confluent.kafka.serializers.protobuf.KafkaProtobufDeserializer
import io.confluent.kafka.serializers.protobuf.KafkaProtobufSerializer
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import reactor.kafka.receiver.KafkaReceiver
import reactor.kafka.receiver.ReceiverOptions
import reactor.kafka.sender.KafkaSender
import reactor.kafka.sender.SenderOptions
import java.util.*

@Configuration
open class BaseKafkaConfig {

    @Value("\${spring.kafka.bootstrap-servers}")
    lateinit var bootstrapServers: String

    @Value("\${spring.kafka.properties.schema.registry.url}")
    lateinit var schemaRegistryUrl: String

    protected open fun baseProducerProperties(
        overrides: MutableMap<String, Any> = mutableMapOf()
    ): MutableMap<String, Any> {
        val baseProperties: MutableMap<String, Any> = mutableMapOf(
            ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to bootstrapServers,
            ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java,
            ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to KafkaProtobufSerializer::class.java,
            "schema.registry.url" to schemaRegistryUrl
        )
        baseProperties.putAll(overrides)
        return baseProperties
    }

    protected open fun baseConsumerProperties(
        overrides: MutableMap<String, Any> = mutableMapOf()
    ): MutableMap<String, Any> {
        val baseProperties: MutableMap<String, Any> = mutableMapOf(
            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to bootstrapServers,
            ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java.name,
            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to KafkaProtobufDeserializer::class.java.name,
            "schema.registry.url" to schemaRegistryUrl
        )
        baseProperties.putAll(overrides)
        return baseProperties
    }

    protected fun <T : GeneratedMessageV3> createKafkaSender(props: MutableMap<String, Any>):
            KafkaSender<String, T> {
        return KafkaSender.create(SenderOptions.create(props))
    }

    protected fun <T : GeneratedMessageV3> createKafkaReceiver(
        props: MutableMap<String, Any>,
        topic: String,
        groupId: String
    ): KafkaReceiver<String, T> {
        props[ConsumerConfig.GROUP_ID_CONFIG] = groupId
        val options =
            ReceiverOptions.create<String, T>(props).subscription(Collections.singleton(topic))
        return KafkaReceiver.create(options)
    }
}
