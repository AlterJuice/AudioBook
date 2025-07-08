package com.alterjuice.test.audiobook.ui.components

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import com.alterjuice.test.audiobook.ui.utils.BaseSideEffect
import com.alterjuice.test.audiobook.ui.utils.EffectHandler
import com.alterjuice.test.audiobook.ui.utils.ShowSnackbarEffect
import com.alterjuice.test.audiobook.ui.utils.rememberEffectHandlerOfType
import com.alterjuice.utils.str.get
import kotlin.collections.get


@Composable
inline fun <reified T: BaseSideEffect> rememberVisibilityEffectHandler(
    key: Any? = true,
    noinline isVisible: (T) -> Boolean,
    initialValue: Boolean = false
): Pair<State<Boolean>, EffectHandler<T>> {
    val lambda = rememberUpdatedState(isVisible)
    val state = remember(key) { mutableStateOf(initialValue) }
    val effectHandler = rememberEffectHandlerOfType<T>(state) { effect ->
        state.value = lambda.value(effect)
    }
    return remember(effectHandler, state) {
        state to effectHandler
    }
}

/**
 * Provides an [EffectHandler] that shows a snackbar using the provided [SnackbarHostState].
 *
 * This is a specialized handler for [ShowSnackbarEffect].
 *
 * @param snackbarHostState The [SnackbarHostState] used to show the snackbar.
 * @return A remembered [EffectHandler] that handles [ShowSnackbarEffect].
 */
@Composable
fun rememberSnackbarEffectHandler(
    snackbarHostState: SnackbarHostState
): EffectHandler<ShowSnackbarEffect> {
    val context = LocalContext.current
    return rememberEffectHandlerOfType(snackbarHostState) { effect ->
        snackbarHostState.showSnackbar(effect.message.get(context))
    }
}