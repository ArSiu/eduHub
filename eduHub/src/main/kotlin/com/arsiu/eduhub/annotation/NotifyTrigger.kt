package com.arsiu.eduhub.annotation

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class NotifyTrigger(val value: String)
