package com.arsiu.eduhub.common.infrastructure.annotation

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class NotifyTrigger(val value: String)
