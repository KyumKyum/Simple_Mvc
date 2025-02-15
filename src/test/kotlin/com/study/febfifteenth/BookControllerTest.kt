package com.study.febfifteenth

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.study.febfifteenth.common.dto.BookDto
import com.study.febfifteenth.common.dto.UpdateBookDto
import com.study.febfifteenth.service.BookService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@SpringBootTest
@AutoConfigureMockMvc
class BookControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var bookService: BookService

    private val objectMapper = jacksonObjectMapper()

    @BeforeEach
    fun setup() {
        bookService.getAllBooks().forEach { book ->
            book.bid?.let { bookService.deleteBook(it) }
        }
    }

    @Test
    fun `should create new book`(){
        val bookDto = BookDto(
            title = "New Title",
            author = "New Author",
            rating = 5,
            genre = "SF"
        )

        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookDto))
        )
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andExpect(MockMvcResultMatchers.jsonPath("$.bid").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(bookDto.title))
            .andExpect(MockMvcResultMatchers.jsonPath("$.author").value(bookDto.author))
            .andExpect(MockMvcResultMatchers.jsonPath("$.rating").value(bookDto.rating))
            .andExpect(MockMvcResultMatchers.jsonPath("$.genre").value(bookDto.genre))
    }

    @Test
    fun `should return validation error for invalid rating`(){
        val invalidBookDto = BookDto(
            title = "New Title",
            author = "New Author",
            rating = 6, // Invalid
            genre = "SF"
        )

        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidBookDto))
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    fun `should return the book by id`(){
        val bookDto = BookDto(
            title = "New Title",
            author = "New Author",
            rating = 5,
            genre = "SF"
        )
         val created = bookService.createBook(bookDto)

        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/books/${created.bid}")
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.bid").value(created.bid))
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(bookDto.title))
    }

    @Test
    fun `should return NOT FOUND (404) when book not found`(){
        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/books/123")
        )
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    fun `should update existing book`(){
        val initialBookDto = BookDto(
            title = "New Title",
            author = "New Author",
            rating = 5,
            genre = "SF"
        )

        val created = bookService.createBook(initialBookDto)

        val updateBookDto = UpdateBookDto(
            title = "Updated Title",
        )

        mockMvc.perform(
            MockMvcRequestBuilders.put("/api/books/${created.bid}")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateBookDto))
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.bid").value(created.bid))
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(updateBookDto.title))
    }

    @Test
    fun `should delete existing book`() {
        val bookDto = BookDto(
            title = "New Title",
            author = "New Author",
            rating = 5,
            genre = "SF"
        )
        val created = bookService.createBook(bookDto)
        mockMvc.perform(
            MockMvcRequestBuilders.delete("/api/books/${created.bid}")
        )
            .andExpect(MockMvcResultMatchers.status().isNoContent)

        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/books/${created.bid}")
        )
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }
}