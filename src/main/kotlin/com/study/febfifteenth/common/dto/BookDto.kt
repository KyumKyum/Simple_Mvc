package com.study.febfifteenth.common.dto

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank

class BookDto (
    val bid: Long? = null, // A Book id written on the tag

    @field:NotBlank(message = "Title should not be left in blank") // Do not allow null, "" and " "
    val title: String,

    @field:NotBlank(message = "Author should not be right in blank")
    val author: String,

    @field:NotBlank(message = "Genre should not be left in blank")
    val genre: String,

    @field:Min(value = 1, message = "Ratings must be between 1 and 5")
    @field:Max(value = 5, message = "Ratings must be between 1 and 5")
    val rating: Int
)

class UpdateBookDto (
    val bid: Long? = null,
    val title: String? = null,
    val author: String? = null,
    val genre: String? = null,
    val rating: Int? = null,
)