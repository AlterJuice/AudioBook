package com.alterjuice.test.audiobook.book_player.model

import com.alterjuice.test.audiobook.book_player.ui.model.AudioBookPlayerState
import com.alterjuice.test.audiobook.domain.model.Book
import kotlinx.coroutines.flow.Flow

/**
 * Aggregates all the operations and state management required for controlling and interacting with an audiobook player.
 *
 * This interface provides unified access to all audiobook player functions, including playback, chapter control,
 * seek, skip, speed adjustments, resource management, and state observation.
 *
 * Example usage:
 * ```
 * val controller: AudioBookPlayerController = ...
 * controller.loadBook(book)
 * controller.play()
 * controller.seekTo(15_000L)
 * controller.selectChapter(2)
 * ```
 *
 */
interface AudioBookPlayerController :
    AudioBookStateHolder,
    AudioBookPlaybackController,
    AudioBookSpeedController,
    AudioBookSeekController,
    AudioBookChapterController,
    AudioBookSkipController,
    AudioBookLoader,
    AudioBookResourceReleaser


/**
 * Holds the current state of the audiobook player.
 */
interface AudioBookStateHolder {
    /**
     * A [Flow] of [com.alterjuice.test.audiobook.book_player.ui.model.AudioBookPlayerState] representing the player's state changes.
     */
    val playerState: Flow<AudioBookPlayerState>
}

/**
 * Releases resources used by the audiobook player.
 */
interface AudioBookResourceReleaser {
    /**
     * Free all player resources and perform any necessary cleanup.
     */
    fun release()
}

/**
 * Loads an audiobook into the player.
 */
interface AudioBookLoader {
    /**
     * Loads the provided [book] for playback.
     *
     * @param book The [Book] to be loaded into the player.
     */
    fun loadBook(book: Book)
}

/**
 * Controls basic playback operations (play and pause).
 */
interface AudioBookPlaybackController {
    /**
     * Start or resume playback.
     */
    fun play()

    /**
     * Pause playback.
     */
    fun pause()
}

/**
 * Controls the playback speed.
 */
interface AudioBookSpeedController {
    /**
     * Set the playback speed.
     *
     * @param speed Playback speed multiplier (e.g., 1.0 for normal speed).
     */
    fun setSpeed(speed: Float)
}

/**
 * Controls seeking within the current chapter or audio.
 */
interface AudioBookSeekController {
    /**
     * Seek to a specific position in milliseconds.
     *
     * @param positionMs The position to seek to, in milliseconds.
     */
    fun seekTo(positionMs: Long)

    /**
     * Seek forward by a predetermined interval.
     */
    fun seekForward()

    /**
     * Seek backward by a predetermined interval.
     */
    fun seekBack()
}

/**
 * Allows skipping between chapters or tracks.
 */
interface AudioBookSkipController {
    /**
     * Skip to the next chapter or track.
     */
    fun skipToNext()

    /**
     * Skip to the previous chapter or track.
     */
    fun skipToPrevious()
}

/**
 * Controller that allows selecting a specific chapter in the audiobook.
 */
interface AudioBookChapterController {
    /**
     * Select the chapter at the specified index.
     *
     * @param index The index of the chapter to select (0-based).
     */
    fun selectChapter(index: Int)
}

