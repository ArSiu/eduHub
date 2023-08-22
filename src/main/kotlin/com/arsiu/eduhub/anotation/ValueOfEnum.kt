package com.arsiu.eduhub.anotation

import com.arsiu.eduhub.anotation.implementation.ValueOfEnumValidator
import jakarta.validation.Constraint
import jakarta.validation.Payload
import jakarta.validation.ReportAsSingleViolation
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Constraint(validatedBy = [ValueOfEnumValidator::class])
@ReportAsSingleViolation
annotation class ValueOfEnum(
    val enumClass: KClass<out Enum<*>>,
    val message: String = "Invalid value for enum",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)
