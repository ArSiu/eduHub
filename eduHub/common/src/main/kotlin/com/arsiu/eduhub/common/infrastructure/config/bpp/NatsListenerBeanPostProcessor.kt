package com.arsiu.eduhub.common.infrastructure.config.bpp

import com.arsiu.eduhub.common.infrastructure.adapters.nats.NatsListener
import com.google.protobuf.GeneratedMessageV3
import org.springframework.beans.factory.config.BeanPostProcessor
import org.springframework.stereotype.Component
import reactor.core.scheduler.Schedulers

@Component
class NatsListenerBeanPostProcessor : BeanPostProcessor {
    override fun postProcessBeforeInitialization(bean: Any, beanName: String): Any {
        if (bean is NatsListener<*, *>) {
            bean.initializeNatsListener()
        }
        return bean
    }

    private fun <ReqT : GeneratedMessageV3, RepT : GeneratedMessageV3>
            NatsListener<ReqT, RepT>.initializeNatsListener() {

        connection.createDispatcher { message ->

            Schedulers.boundedElastic().schedule {
                val request = parser.parseFrom(message.data)
                handle(request)
            }

        }.subscribe(subject)
    }
}
