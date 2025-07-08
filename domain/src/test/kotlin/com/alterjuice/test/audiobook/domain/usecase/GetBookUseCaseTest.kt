package com.alterjuice.test.audiobook.domain.usecase

import com.alterjuice.test.audiobook.domain.model.Book
import com.alterjuice.test.audiobook.domain.repository.BookRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class GetBookUseCaseTest {
    private val repository: BookRepository = mockk()

    private val useCase = GetBookUseCase(repository)

    @Test
    fun `GetBookUseCase invoke SHOULD call repository's getBook`() = runTest {
        // GIVEN
        val testBookId = "TestBook"
        val fakeBook: Book = mockk()

        coEvery { repository.getBook(testBookId) } returns Result.success(fakeBook)

        // WHEN
        useCase("TestBook")

        // THEN
        coVerify(exactly = 1) { repository.getBook(testBookId) }

    }
}