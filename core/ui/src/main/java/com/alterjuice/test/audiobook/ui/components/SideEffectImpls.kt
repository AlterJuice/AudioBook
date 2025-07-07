package com.alterjuice.test.audiobook.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import com.alterjuice.test.audiobook.ui.utils.BaseSideEffect
import com.alterjuice.test.audiobook.ui.utils.EffectHandler
import com.alterjuice.test.audiobook.ui.utils.rememberEffectHandlerOfType


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