package com.alterjuice.test.audiobook.data.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BookDto(
    @SerialName("book_id") val id: String,
    @SerialName("book_title") val title: String,
    @SerialName("author_name") val author: String,
    @SerialName("language") val language: String,
    @SerialName("description") val description: String,
    @SerialName("cover_image") val coverArtUrl: String,
    @SerialName("chapter_list") val chapters: List<ChapterDto>
)