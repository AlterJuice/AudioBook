package com.alterjuice.test.audiobook.book_player.ui.model

import androidx.compose.runtime.Immutable
import com.alterjuice.test.audiobook.domain.model.Book
import com.alterjuice.test.audiobook.domain.model.Chapter

@Immutable
data class AudioBookPlayerState(
    val isPlaying: Boolean = false,
    val currentPositionMs: Long = 0L,
    val totalDurationMs: Long = 0L,
    val playbackSpeed: Float = 1.0f,
    val bookCoverUrl: String = "",
    val book: Book? = null,
    val currentChapter: Chapter? = null,
    val isLoading: Boolean = true,
    val error: String? = null
)