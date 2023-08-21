package com.arsiu.eduhub.controller.advice

import com.arsiu.eduhub.exception.NotFoundException
import com.fasterxml.jackson.databind.exc.InvalidFormatException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ExceptionControllerAdvice {

    @ExceptionHandler(NotFoundException::class)
    fun handleNotFoundExceptionException(ex: NotFoundException): ResponseEntity<ErrorMessage> =
        ResponseEntity(
            ErrorMessage(HttpStatus.NOT_FOUND.value(), ex.message),
            HttpStatus.BAD_REQUEST
        )


    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationErrors(ex: MethodArgumentNotValidException): ResponseEntity<List<String>> =
        ResponseEntity(
            ex.bindingResult.fieldErrors.map { it.defaultMessage ?: "" }.toList(),
            HttpHeaders(),
            HttpStatus.BAD_REQUEST
        )

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleValidationErrors(ex: HttpMessageNotReadableException) =
        ResponseEntity(
            (ex.rootCause as? InvalidFormatException) ?: ex.message ?: "Bad request",
            HttpHeaders(),
            HttpStatus.BAD_REQUEST
        )
}

class ErrorMessage(var status: Int? = null, var message: String? = null)