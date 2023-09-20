package com.arsiu.eduhub.config.bpp

import com.arsiu.eduhub.controller.nats.NatsController
import com.google.protobuf.GeneratedMessageV3
import org.springframework.beans.factory.config.BeanPostProcessor
import org.springframework.stereotype.Component

@Component
class NatsControllerBeanPostProcessor : BeanPostProcessor {

    override fun postProcessBeforeInitialization(bean: Any, beanName: String): Any {
        if (bean is NatsController<*, *>) {
            bean.initializeNatsController()
        }
        return bean
    }

    private fun <ReqT : GeneratedMessageV3, RepT : GeneratedMessageV3>
            NatsController<ReqT, RepT>.initializeNatsController() {
        connection.createDispatcher { message ->
            val parsedData = parser.parseFrom(message.data)
            val response = handler(parsedData)
            connection.publish(message.replyTo, response.toByteArray())
        }.subscribe(subject)
    }
}
