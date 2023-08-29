package com.arsiu.eduhub.bpp

import com.arsiu.eduhub.annotation.NotifyTrigger
import com.arsiu.eduhub.model.enums.Role
import com.arsiu.eduhub.service.UserService
import com.arsiu.eduhub.service.util.mail.EmailParameters
import com.arsiu.eduhub.service.util.mail.EmailServiceImpl
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.springframework.beans.factory.config.BeanPostProcessor
import org.springframework.stereotype.Component
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
            Proxy.newProxyInstance(clazz.java.classLoader, clazz.java.interfaces) { _, method, args ->
                val result = method.invoke(bean, *(args ?: emptyArray()))
                getAnnotation(clazz, method)?.let { notifyUsers(it.value + result.toString()) }
                result
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

    @OptIn(DelicateCoroutinesApi::class)
    private fun notifyUsers(msg: String) {
        GlobalScope.launch {
            userService.findAll()
                .filter { it.role == Role.STUDENT }
                .forEach { emailServiceImpl.sendMail(EmailParameters(it.email, msg, msg)) }
        }
    }

}
