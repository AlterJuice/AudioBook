package com.alterjuice.test.audiobook.domain.usecase

import com.alterjuice.test.audiobook.domain.model.Book
import com.alterjuice.test.audiobook.domain.repository.BookRepository
import javax.inject.Inject


class GetBookUseCase @Inject constructor(
    private val bookRepository: BookRepository
) {
    suspend operator fun invoke(bookId: String): Result<Book> {
        return bookRepository.getBook(bookId)
    }
}
