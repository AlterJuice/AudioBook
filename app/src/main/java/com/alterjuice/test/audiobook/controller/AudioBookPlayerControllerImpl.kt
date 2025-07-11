package com.alterjuice.test.audiobook.controller

import android.content.ComponentName
import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.PlaybackParameters
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.alterjuice.test.audiobook.book_player.model.AudioBookPlayerController
import com.alterjuice.test.audiobook.book_player.ui.model.AudioBookPlayerState
import com.alterjuice.test.audiobook.book_player.utils.toSortedMediaItems
import com.alterjuice.test.audiobook.domain.model.Book
import com.alterjuice.test.audiobook.errors.AppError
import com.alterjuice.test.audiobook.service.AudioBookPlayerService
import com.alterjuice.test.audiobook.utils.other.RestartableJob
import com.alterjuice.test.audiobook.utils.other.RestartableJobArgs
import com.google.common.util.concurrent.ListenableFuture
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.ticker
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

class AudioBookPlayerControllerImpl @Inject constructor(
    @ApplicationContext context: Context,
) : AudioBookPlayerController, Player.Listener {
    private val exceptionHandler = CoroutineExceptionHandler { ctx, throwable ->
        _error.tryEmit(AppError(null, throwable))
    }
    private val scope = CoroutineScope(Dispatchers.Main + exceptionHandler)

    private var mediaController: MediaController? = null
    private val _playerState = MutableStateFlow(AudioBookPlayerState())
    override val playerState: StateFlow<AudioBookPlayerState> = _playerState.asStateFlow()

    private val _error = MutableSharedFlow<AppError>(extraBufferCapacity = 10)
    override val error = _error.asSharedFlow()

    private val positionUpdateJob = RestartableJob {
        ticker(1000, initialDelayMillis = 0).receiveAsFlow().cancellable().collect {
            _playerState.update {
                it.copy(
                    currentPositionMs = mediaController?.currentPosition
                        ?: it.currentPositionMs
                )
            }
        }
    }
    private val bookLoaderJob = RestartableJobArgs<Book> { book ->
        val mediaItems = book.toSortedMediaItems()
        val controller = controllerFuture.await()
        ensureActive()
        controller.setMediaItems(mediaItems)
        controller.prepare()
        _playerState.update {
            it.copy(
                book = book,
                currentChapter = book.chapters.firstOrNull()
            )
        }
    }

    val controllerFuture = MediaController.Builder(
        context,
        SessionToken(
            context,
            ComponentName(
                context,
                AudioBookPlayerService::class.java
            )
        )
    ).buildAsync()

    init {
        scope.launch {
            mediaController = controllerFuture.await().also {
                it.addListener(this@AudioBookPlayerControllerImpl)
            }
            refreshCurrentMediaItemInformation()
        }
    }



    override fun loadBook(book: Book) {
        bookLoaderJob.restart(scope, Dispatchers.Main, value = book)
    }

    override suspend fun isBookLoaded(bookId: String): Boolean {
        val controller = controllerFuture.await()
        return controller.mediaItemCount > 0 && playerState.value.book?.id == bookId
    }

    override fun play() {
        mediaController?.play()
    }

    override fun pause() {
        mediaController?.pause()
    }


    private fun updateState() {
        _playerState.update {
            it.copy(
                isPlaying = mediaController?.isPlaying ?: false,
                currentPositionMs = mediaController?.currentPosition ?: 0L,
                totalDurationMs = mediaController?.duration ?: 0L,
                bookCoverUrl = mediaController?.currentMediaItem?.mediaMetadata?.artworkUri?.toString()
                    .orEmpty(),
                currentChapter = it.book?.chapters?.find { chapter ->
                    chapter.order == (mediaController?.currentMediaItemIndex ?: 0)
                },
            )
        }
    }


    override fun setSpeed(speed: Float) {
        mediaController?.setPlaybackSpeed(speed)
    }

    override fun selectChapter(index: Int) {
        mediaController?.seekTo(index, 0)
    }

    override fun seekTo(positionMs: Long) {
        mediaController?.seekTo(positionMs)
    }

    override fun seekForward() {
        mediaController?.seekForward()
    }

    override fun seekBack() {
        mediaController?.seekBack()
    }

    override fun skipToNext() {
        mediaController?.seekToNext()
    }

    override fun skipToPrevious() {
        mediaController?.seekToPrevious()
    }

    override fun release() {
        mediaController?.release()
        mediaController?.removeListener(this)
        mediaController = null
        scope.cancel()
    }

    override fun onPlayerError(error: PlaybackException) {
        _error.tryEmit(AppError(null, error))
    }

    override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters) {
        _playerState.update {
            it.copy(playbackSpeed = playbackParameters.speed)
        }
    }

    override fun onIsPlayingChanged(isPlaying: Boolean) {
        refreshCurrentMediaItemInformation(isPlaying)
    }

    override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
        refreshCurrentMediaItemInformation()
    }

    private fun refreshCurrentMediaItemInformation(
        isPlaying: Boolean = mediaController?.isPlaying ?: false,
    ) {
        updateState()
        handlePositionUpdates(isPlaying)
    }

    private fun handlePositionUpdates(isPlaying: Boolean) {
        positionUpdateJob.cancel()
        if (isPlaying) {
            positionUpdateJob.restart(scope)
        }
    }
}

private suspend fun <E> ListenableFuture<E>.await(): E {
    val executor = currentCoroutineContext()[CoroutineDispatcher]!!.asExecutor()
    return suspendCancellableCoroutine<E> { cont ->
        this.addListener({ cont.resume(this.get()) }, executor)
    }
}