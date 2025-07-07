package com.alterjuice.test.audiobook.data.network.service

import com.alterjuice.test.audiobook.data.network.model.BookDto
import com.alterjuice.test.audiobook.data.network.test.WizardOfOzData
import javax.inject.Inject

internal class BookApiServiceTestMock @Inject constructor(): BookApiService {
    override suspend fun getBookById(id: String): BookDto {
        return WizardOfOzData
    }
}