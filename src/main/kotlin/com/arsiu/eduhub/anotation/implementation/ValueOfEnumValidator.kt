package com.arsiu.eduhub.anotation.implementation

import com.arsiu.eduhub.anotation.ValueOfEnum
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class ValueOfEnumValidator : ConstraintValidator<ValueOfEnum, Enum<*>> {
    private lateinit var enumValues: List<Enum<*>>

    override fun initialize(constraintAnnotation: ValueOfEnum) {
        val enumClass = constraintAnnotation.enumClass
        enumValues = enumClass.java.enumConstants.toList()
    }

    override fun isValid(value: Enum<*>?, context: ConstraintValidatorContext?): Boolean {
        if (value != null && value !in enumValues) {
            val fieldName = context?.defaultConstraintMessageTemplate?.substringAfterLast(".") ?: ""
            val validValues = enumValues.joinToString(", ") { it.name }

            val errorMessage =
                """ "$value" is not one of the valid values for field "$fieldName". Valid values are: $validValues """

            context?.disableDefaultConstraintViolation()
            context?.buildConstraintViolationWithTemplate(errorMessage)?.addConstraintViolation()

            return false
        }

        return true
    }
}
