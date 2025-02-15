package com.study.febfifteenth.exception

import com.study.febfifteenth.common.exception.BookNotFoundException
import com.study.febfifteenth.common.exception.InvalidBookDataException
import com.study.febfifteenth.common.exception.SimpleErrorResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class BookExceptionHandler { // ExceptionHandler

    @ExceptionHandler(BookNotFoundException::class)
    fun handleBookNotFound(ex: BookNotFoundException): ResponseEntity<SimpleErrorResponse> =
        ResponseEntity.status(HttpStatus.NOT_FOUND).body(SimpleErrorResponse(ex.message ?: "Book not found"))

    @ExceptionHandler(InvalidBookDataException::class)
    fun handleInvalidBookDataException(ex: InvalidBookDataException): ResponseEntity<SimpleErrorResponse> =
        ResponseEntity.status(HttpStatus.BAD_REQUEST).body(SimpleErrorResponse(ex.message ?: "Invalid book data"))
}