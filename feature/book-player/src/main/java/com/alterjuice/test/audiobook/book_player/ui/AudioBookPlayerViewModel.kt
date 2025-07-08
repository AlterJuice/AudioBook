package com.alterjuice.test.audiobook.book_player.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alterjuice.test.audiobook.book_player.model.AudioBookPlayerController
import com.alterjuice.test.audiobook.book_player.ui.model.AudioBookPlayerState
import com.alterjuice.test.audiobook.book_player.ui.model.PlayerEvent
import com.alterjuice.test.audiobook.book_player.ui.model.PlayerSideEffects
import com.alterjuice.test.audiobook.domain.usecase.GetBookUseCase
import com.alterjuice.test.audiobook.errors.GlobalError
import com.alterjuice.test.audiobook.errors.RootAppErrorMessagesProvider
import com.alterjuice.test.audiobook.errors.isAppError
import com.alterjuice.test.audiobook.ui.utils.BaseSideEffect
import com.alterjuice.test.audiobook.ui.utils.ShowSnackbarEffect
import com.alterjuice.test.audiobook.utils.other.RestartableJob
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AudioBookPlayerViewModel @Inject constructor (
    private val getBookUseCase: GetBookUseCase,
    private val audioPlayer: AudioBookPlayerController,
    private val appErrorsHandler: RootAppErrorMessagesProvider
) : ViewModel() {


    val state: StateFlow<AudioBookPlayerState> = audioPlayer.playerState
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = AudioBookPlayerState()
        )
    private val audioPlayerErrorCollectorJob = RestartableJob {
        audioPlayer.error.cancellable().collect {
            setError(it)
        }
    }

    private val _effect = MutableSharedFlow<BaseSideEffect>()
    val effect = _effect.asSharedFlow()


    init {
        audioPlayerErrorCollectorJob.restart(viewModelScope, Dispatchers.Main)
        loadBookData()
    }

    private fun loadBookData() {
        viewModelScope.launch {
            val bookId = "wizard_of_oz"
            if (audioPlayer.isBookLoaded(bookId)) {
                return@launch
            }
            val result = getBookUseCase(bookId)
            result.onSuccess { book ->
                audioPlayer.loadBook(book)
            }.onFailure { error ->
                setError(error)
            }
        }
    }

    fun onEvent(event: PlayerEvent) {
        when (event) {
            is PlayerEvent.Seek.To -> audioPlayer.seekTo(event.seekTo)
            PlayerEvent.Seek.Forward -> audioPlayer.seekForward()
            PlayerEvent.Seek.Backward -> audioPlayer.seekBack()
            PlayerEvent.Pause -> audioPlayer.pause()
            PlayerEvent.Play -> audioPlayer.play()
            is PlayerEvent.SetSpeed -> audioPlayer.setSpeed(event.speed)
            PlayerEvent.ToNext -> audioPlayer.skipToNext()
            PlayerEvent.ToPrevious -> audioPlayer.skipToPrevious()
            PlayerEvent.HideSpeedPicker -> updateSpeedPickerVisibility(false)
            PlayerEvent.ShowSpeedPicker -> updateSpeedPickerVisibility(true)
            is PlayerEvent.ToChapter -> audioPlayer.selectChapter(event.index)
            PlayerEvent.HideChapterPicker -> updateChapterPickerVisibility(false)
            PlayerEvent.ShowChapterPicker -> updateChapterPickerVisibility(true)
        }
    }

    private fun updateSpeedPickerVisibility(isVisible: Boolean) {
        sendSideEffect(PlayerSideEffects.UpdateSpeedPicker(isVisible))
    }

    private fun updateChapterPickerVisibility(isVisible: Boolean) {
        sendSideEffect(PlayerSideEffects.UpdateChapterPicker(isVisible))
    }

    private fun setError(error: Throwable) {
        val appError = if (error.isAppError()) error else GlobalError.Unknown(error)
        viewModelScope.launch {
            _effect.emit(ShowSnackbarEffect(appErrorsHandler.provideMessage(appError)))
        }
    }

    private fun sendSideEffect(effect: PlayerSideEffects) {
        viewModelScope.launch {
            _effect.emit(effect)
        }
    }

    override fun onCleared() {
        audioPlayer.release()
        super.onCleared()
    }
}