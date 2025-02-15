package com.study.febfifteenth.service

import com.study.febfifteenth.common.dto.BookDto
import com.study.febfifteenth.common.dto.UpdateBookDto


interface BookService {
    fun getAllBooks(): List<BookDto>
    fun getBookById(bid: Long): BookDto
    fun createBook(bookDto: BookDto): BookDto
    fun updateBook(bid: Long, bookDto: UpdateBookDto): BookDto
    fun deleteBook(bid: Long)
}