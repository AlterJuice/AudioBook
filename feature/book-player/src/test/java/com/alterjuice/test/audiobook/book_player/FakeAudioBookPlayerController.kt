package com.alterjuice.test.audiobook.book_player

import com.alterjuice.test.audiobook.book_player.model.AudioBookPlayerController
import com.alterjuice.test.audiobook.book_player.ui.model.AudioBookPlayerState
import com.alterjuice.test.audiobook.domain.model.Book
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class FakeAudioBookPlayerController : AudioBookPlayerController {

    private val _playerState = MutableStateFlow(AudioBookPlayerState())
    override val playerState = _playerState.asStateFlow()

    var playCallCount = 0
    var pauseCallCount = 0
    var lastSetSpeedValue: Float? = null

    fun simulateIsPlaying(isPlaying: Boolean) {
        _playerState.value = _playerState.value.copy(isPlaying = isPlaying)
    }

    override fun loadBook(book: Book) {
        _playerState.value = _playerState.value.copy(book = book)
    }

    override fun play() {
        playCallCount++
    }

    override fun pause() {
        pauseCallCount++
    }

    override fun setSpeed(speed: Float) {
        lastSetSpeedValue = speed
    }

    override fun seekTo(positionMs: Long) {}
    override fun seekForward() {}
    override fun seekBack() {}
    override fun skipToNext() {}
    override fun skipToPrevious() {}
    override fun selectChapter(index: Int) {}
    override fun release() {}
}