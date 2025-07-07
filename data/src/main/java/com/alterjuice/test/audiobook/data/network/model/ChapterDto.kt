package com.alterjuice.test.audiobook.data.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChapterDto(
    @SerialName("chapter_id") val id: String,
    @SerialName("chapter_title") val title: String,
    @SerialName("audio_link") val audioUrl: String,
    @SerialName("chapter_order") val order: Int
)
