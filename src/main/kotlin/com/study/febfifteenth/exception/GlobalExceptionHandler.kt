package com.study.febfifteenth.exception

import com.study.febfifteenth.common.exception.SimpleErrorResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationErrors(ex: MethodArgumentNotValidException): ResponseEntity<SimpleErrorResponse> {
        val errors = ex.bindingResult.fieldErrors.joinToString(", ") { fieldError ->
            "${fieldError.field}: ${fieldError.defaultMessage}"
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(SimpleErrorResponse(errors))
    }
}