package com.alterjuice.test.audiobook.data.network.mapper

import com.alterjuice.test.audiobook.data.network.model.BookDto
import com.alterjuice.test.audiobook.data.network.model.ChapterDto
import com.alterjuice.test.audiobook.domain.model.Book
import com.alterjuice.test.audiobook.domain.model.Chapter
import kotlinx.collections.immutable.toImmutableList

fun BookDto.toDomain(): Book {
    return Book(
        id = this.id,
        title = this.title,
        author = this.author,
        coverArtUrl = this.coverArtUrl,
        language = this.language,
        description = this.description,
        chapters = this.chapters.map { it.toDomain() }.toImmutableList()
    )
}

fun ChapterDto.toDomain(): Chapter {
    return Chapter(
        id = this.id,
        title = this.title,
        audioUrl = this.audioUrl,
        order = this.order
    )
}