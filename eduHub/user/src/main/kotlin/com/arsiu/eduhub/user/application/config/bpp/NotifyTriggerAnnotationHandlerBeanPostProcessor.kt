package com.arsiu.eduhub.user.application.config.bpp

import com.arsiu.eduhub.common.application.annotation.NotifyTrigger
import com.arsiu.eduhub.common.infrastructure.nontification.mail.EmailParameters
import com.arsiu.eduhub.common.infrastructure.nontification.mail.EmailServiceImpl
import com.arsiu.eduhub.user.application.ports.UserService
import com.arsiu.eduhub.user.domain.Role
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.config.BeanPostProcessor
import org.springframework.stereotype.Component
import reactor.core.scheduler.Schedulers
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
import java.lang.reflect.Proxy
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberFunctions

@Component
@Suppress("SpreadOperator")
class NotifyTriggerAnnotationHandlerBeanPostProcessor(
    private val userService: UserService,
    private val emailServiceImpl: EmailServiceImpl
) : BeanPostProcessor {

    private val logger =
        LoggerFactory.getLogger(NotifyTriggerAnnotationHandlerBeanPostProcessor::class.java)

    private val beans = mutableMapOf<String, KClass<*>>()

    override fun postProcessBeforeInitialization(bean: Any, beanName: String): Any? {
        val beanClass = bean::class

        beanClass.java.methods.forEach {
            it.getAnnotation(NotifyTrigger::class.java)?.let {
                beans[beanName] = beanClass
            }
        }

        return bean
    }

    override fun postProcessAfterInitialization(bean: Any, beanName: String): Any? {
        val beanClass = beans[beanName]

        return beanClass?.let { clazz ->
            Proxy.newProxyInstance(
                clazz.java.classLoader,
                clazz.java.interfaces
            ) { _, method, args ->
                try {
                    val result = method.invoke(bean, *(args ?: emptyArray()))
                    getAnnotation(clazz, method)?.let { notifyUsers(it.value) }
                    return@newProxyInstance result
                } catch (e: InvocationTargetException) {
                    logger.error(e.targetException.toString())
                    throw e
                }
            }
        } ?: bean
    }

    private fun getAnnotation(beanClass: KClass<*>, method: Method): NotifyTrigger? {
        return beanClass.memberFunctions.firstOrNull {
            (it.name == method.name)
                    &&
                    (it.javaClass.typeParameters.contentEquals(method.javaClass.typeParameters))
        }?.findAnnotation<NotifyTrigger>()
    }

    private fun notifyUsers(msg: String) {
        userService.findAll()
            .filter { it.role == Role.STUDENT }
            .parallel().runOn(Schedulers.boundedElastic())
            .doOnNext { emailServiceImpl.sendMail(EmailParameters(it.email, msg, msg)) }
            .subscribe()
    }

}
