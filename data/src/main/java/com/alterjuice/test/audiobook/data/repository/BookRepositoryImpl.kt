package com.alterjuice.test.audiobook.data.repository


import com.alterjuice.test.audiobook.data.network.mapper.toDomain
import com.alterjuice.test.audiobook.data.network.service.BookApiService
import com.alterjuice.test.audiobook.domain.model.Book
import com.alterjuice.test.audiobook.domain.repository.BookRepository
import javax.inject.Inject


internal class BookRepositoryImpl @Inject constructor(
    private val apiService: BookApiService
) : BookRepository {

    override suspend fun getBook(id: String): Result<Book> = runCatching {
        val bookDto = apiService.getBookById(id)
        bookDto.toDomain()
    }
}

