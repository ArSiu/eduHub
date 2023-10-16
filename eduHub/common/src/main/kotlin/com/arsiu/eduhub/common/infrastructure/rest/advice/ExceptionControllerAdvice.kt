package com.arsiu.eduhub.common.infrastructure.rest.advice

import com.arsiu.eduhub.common.application.exception.NotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ExceptionControllerAdvice {

    @ExceptionHandler(NotFoundException::class)
    fun handleNotFoundException(ex: NotFoundException): ResponseEntity<ErrorMessage> =
        ResponseEntity(
            ErrorMessage(NOT_FOUND.value(), ex.message ?: "Bad request"),
            HttpStatus.BAD_REQUEST
        )

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(ex: MethodArgumentNotValidException): ResponseEntity<ErrorMessage> =
        ResponseEntity(
            ErrorMessage(
                BAD_REQUEST.value(),
                ex.bindingResult.fieldErrors.map { it.defaultMessage ?: "" }.toString()
            ),
            HttpStatus.BAD_REQUEST
        )

}

class ErrorMessage(val status: Int, val message: String)
