package com.study.febfifteenth.implementation

import com.study.febfifteenth.common.dto.BookDto
import com.study.febfifteenth.common.dto.UpdateBookDto
import com.study.febfifteenth.common.exception.BookNotFoundException
import com.study.febfifteenth.common.utils.LogAggregator
import com.study.febfifteenth.service.BookService
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong

@Service
@CacheConfig(cacheNames = ["bookService"])
class BookServiceImpl(
    private val logAggregator: LogAggregator
): BookService {
    private val books = ConcurrentHashMap<Long, BookDto>() // Demo; This should be connected to persistence layer later.
    private val seq = AtomicLong(1)

    override fun getAllBooks(): List<BookDto> = books.values.toList()

    // Check if bid is in cache. (If not cached, retrives from db and caches it.
    @Cacheable(key = "#bid", condition = "#bid != null")
    override fun getBookById(bid: Long): BookDto = books[bid] ?: throw BookNotFoundException(bid)

    override fun createBook(bookDto: BookDto): BookDto {
        val id = seq.getAndIncrement()
        val newBook = BookDto(
            bid = id,
            title = bookDto.title,
            author = bookDto.author,
            rating = bookDto.rating,
            genre = bookDto.genre,
        )
        books[id] = newBook
        logAggregator.relayInfo("Book Created: ${newBook.bid}")
        return newBook
    }

    @CachePut(key = "#bid")
    override fun updateBook(bid: Long, bookDto: UpdateBookDto): BookDto {
        val existingBook = books[bid] ?: throw BookNotFoundException(bid)
        val updatedBook = BookDto(
            bid = bid,
            title = bookDto.title ?: existingBook.title,
            author = bookDto.author ?: existingBook.author,
            rating = bookDto.rating ?: existingBook.rating,
            genre = bookDto.genre ?: existingBook.genre,
        )

        books[bid] = updatedBook
        return updatedBook
    }

    @CacheEvict(key = "#bid")
    override fun deleteBook(bid: Long) {
        books.remove(bid) ?: throw BookNotFoundException(bid)
    }


    // Global Cache Eviction
//    @CacheEvict(allEntries = true) // Evict all entries
//    @Scheduled(fixedDelay = 60000) // Evict for every 1 min
//    fun evictCache(){}
}