package com.alterjuice.test.audiobook.book_player.ui.model

sealed interface PlayerEvent {
    data object ShowChapterPicker: PlayerEvent
    data object HideChapterPicker: PlayerEvent
    data object ShowSpeedPicker: PlayerEvent
    data object HideSpeedPicker: PlayerEvent

    data class SetSpeed(val speed: Float): PlayerEvent
    data object Play: PlayerEvent
    data object Pause: PlayerEvent
    sealed interface Seek: PlayerEvent {
        data class To(val seekTo: Long) : Seek
        data object Backward : Seek
        data object Forward: Seek
    }
    data object ToNext: PlayerEvent
    data object ToPrevious: PlayerEvent
    data class ToChapter(val index: Int): PlayerEvent
}
