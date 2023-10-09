package com.arsiu.eduhub.common.application.annotation

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class NotifyTrigger(val value: String)
