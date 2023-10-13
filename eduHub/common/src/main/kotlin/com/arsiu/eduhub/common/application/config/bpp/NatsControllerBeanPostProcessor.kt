package com.arsiu.eduhub.common.application.config.bpp

import com.arsiu.eduhub.common.application.port.nats.NatsController
import com.arsiu.eduhub.common.application.port.protobuf.handler.Handler
import com.google.protobuf.GeneratedMessageV3
import org.springframework.beans.factory.config.BeanPostProcessor
import org.springframework.stereotype.Component
import reactor.core.scheduler.Schedulers

@Component
class NatsControllerBeanPostProcessor : BeanPostProcessor {

    override fun postProcessBeforeInitialization(bean: Any, beanName: String): Any {
        if (bean is NatsController<*, *, *>) {
            bean.initializeNatsController()
        }
        return bean
    }

    private fun <ReqT : GeneratedMessageV3, RepT : GeneratedMessageV3, H : Handler<ReqT, RepT>>
            NatsController<ReqT, RepT, H>.initializeNatsController() {

        connection.createDispatcher { message ->

            val any = com.google.protobuf.Any.parseFrom(message.data)

            if (!any.`is`(type)) {
                connection.publish(
                    message.replyTo,
                    handler.failureResponse(
                        "Invalid Message",
                        "Message type mismatch or you dont use Any.pack()"
                    ).toByteArray()
                )
                return@createDispatcher
            }

            handle(parser.parseFrom(any.unpack(type).toByteArray()))
                .subscribeOn(Schedulers.boundedElastic())
                .publishOn(Schedulers.boundedElastic())
                .map { it.toByteArray() }
                .doOnNext { connection.publish(message.replyTo, it) }
                .subscribe()
        }.subscribe(subject)
    }

}
