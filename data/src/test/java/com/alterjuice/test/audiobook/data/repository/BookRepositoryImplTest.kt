package com.alterjuice.test.audiobook.data.repository

import com.alterjuice.test.audiobook.data.network.model.BookDto
import com.alterjuice.test.audiobook.data.network.model.ChapterDto
import com.alterjuice.test.audiobook.data.network.service.BookApiService
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertTrue
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class BookRepositoryImplTest {

    @Test
    fun `getBook success SHOULD return success Result with mapped Book`() = runTest {
        // GIVEN
        val apiService: BookApiService = mockk()
        val repository = BookRepositoryImpl(apiService)

        val fakeBookDto = BookDto(
            id = "1",
            title = "Test Book",
            author = "Test Author",
            coverArtUrl = "",
            chapters = persistentListOf<ChapterDto>(),
            language = "English",
            description = "Test description"
        )
        coEvery { apiService.getBookById(any()) } returns fakeBookDto

        // WHEN
        val result = repository.getBook("1")

        // THEN
        assertTrue(result.isSuccess)
        assertEquals("Test Book", result.getOrNull()?.title)
    }

    @Test
    fun `getBook failure SHOULD return failure Result`() = runTest {
        // GIVEN
        val apiService: BookApiService = mockk()
        val repository = BookRepositoryImpl(apiService)
        val exception = RuntimeException("Network Error")

        coEvery { apiService.getBookById(any()) } throws exception

        // WHEN
        val result = repository.getBook("1")

        // THEN
        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
    }
}
