package com.arsiu.eduhub.controller.advice

import com.arsiu.eduhub.exception.NotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ExceptionControllerAdvice {

    @ExceptionHandler(NotFoundException::class)
    fun handleNotFoundException(ex: NotFoundException): ResponseEntity<ErrorMessage> =
        ResponseEntity(
            ErrorMessage(HttpStatus.NOT_FOUND.value(), ex.message ?: "Bad request"),
            HttpStatus.BAD_REQUEST
        )

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(ex: MethodArgumentNotValidException): ResponseEntity<ErrorMessage> =
        ResponseEntity(
            ErrorMessage(
                HttpStatus.BAD_REQUEST.value(),
                ex.bindingResult.fieldErrors.map { it.defaultMessage ?: "" }.toString()
            ),
            HttpStatus.BAD_REQUEST
        )

}

class ErrorMessage(val status: Int, val message: String)
