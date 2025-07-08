package com.alterjuice.test.audiobook.ui.utils

import com.alterjuice.utils.str.Str


/**
 * Marker interface for all side effects in MVI architecture.
 *
 * Side effects represent one-time UI actions that are not part of the state,
 * such as navigation, toasts, or snackbars.
 */
interface BaseSideEffect


/**
 * A concrete implementation of [BaseSideEffect] used to display a snackbar.
 *
 * @param message The message to be shown.
 */
data class ShowSnackbarEffect(val message: Str) : BaseSideEffect