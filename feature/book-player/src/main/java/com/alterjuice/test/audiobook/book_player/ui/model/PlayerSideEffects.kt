package com.alterjuice.test.audiobook.book_player.ui.model

import com.alterjuice.test.audiobook.ui.utils.BaseSideEffect

sealed interface PlayerSideEffects: BaseSideEffect {
    data class UpdateSpeedPicker(val isVisible: Boolean): PlayerSideEffects
    data class UpdateChapterPicker(val isVisible: Boolean): PlayerSideEffects
}