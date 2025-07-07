package com.alterjuice.test.audiobook.data.network.service

import com.alterjuice.test.audiobook.data.network.model.BookDto

interface BookApiService {
    suspend fun getBookById(id: String): BookDto
}


