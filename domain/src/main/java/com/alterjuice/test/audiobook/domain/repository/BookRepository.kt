package com.alterjuice.test.audiobook.domain.repository

import com.alterjuice.test.audiobook.domain.model.Book

interface BookRepository {
    suspend fun getBook(id: String): Result<Book>
}