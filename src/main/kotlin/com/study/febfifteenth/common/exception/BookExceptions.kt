package com.study.febfifteenth.common.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
class BookNotFoundException(bid: Long): RuntimeException("Book Not Found: $bid") // Using unchecked exception; Trivial Error - Not forcing to catch the exception

@ResponseStatus(HttpStatus.BAD_REQUEST)
class InvalidBookDataException(msg: String): RuntimeException(msg)