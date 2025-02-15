package com.study.febfifteenth.controller

import com.study.febfifteenth.common.dto.BookDto
import com.study.febfifteenth.common.dto.UpdateBookDto
import com.study.febfifteenth.service.BookService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/books")
class BookController(
    val bookService: BookService
) {

    @GetMapping
    fun getAllBooks(): ResponseEntity<List<BookDto>> {
        return ResponseEntity.ok(bookService.getAllBooks())
    }

    @GetMapping("/{id}")
    fun getBookById(@PathVariable("id") id: Long): ResponseEntity<BookDto> {
        return ResponseEntity.ok(bookService.getBookById(id))
    }

    @PostMapping
    fun createBook(@Valid @RequestBody bookDto: BookDto): ResponseEntity<BookDto> {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookService.createBook(bookDto))
    }

    @PutMapping("/{id}")
    fun updateBook(
        @PathVariable("id") id: Long,
        @Valid @RequestBody bookDto: UpdateBookDto
    ): ResponseEntity<BookDto> {
        return ResponseEntity.ok(bookService.updateBook(id, bookDto))
    }

    @DeleteMapping("/{id}")
    fun deleteBook(@PathVariable("id") id: Long): ResponseEntity<Void> =
        ResponseEntity.noContent().build<Void>().also {
            bookService.deleteBook(id)
        }

}