package com.alterjuice.test.audiobook.book_player.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alterjuice.test.audiobook.book_player.BuildConfig
import com.alterjuice.test.audiobook.book_player.ui.components.AudioPlaybackControls
import com.alterjuice.test.audiobook.book_player.ui.components.AudioPlayerSlider
import com.alterjuice.test.audiobook.book_player.ui.components.BookCover
import com.alterjuice.test.audiobook.book_player.ui.components.ChapterInfo
import com.alterjuice.test.audiobook.book_player.ui.components.ChapterSelectionBottomSheet
import com.alterjuice.test.audiobook.book_player.ui.components.SpeedButton
import com.alterjuice.test.audiobook.book_player.ui.components.SpeedSelectionBottomSheet
import com.alterjuice.test.audiobook.book_player.ui.model.AudioBookPlayerState
import com.alterjuice.test.audiobook.book_player.ui.model.AudioPlayerContents
import com.alterjuice.test.audiobook.book_player.ui.model.PlayerEvent
import com.alterjuice.test.audiobook.book_player.ui.model.PlayerSideEffects
import com.alterjuice.test.audiobook.ui.components.HorizontallyAnimatedContent
import com.alterjuice.test.audiobook.ui.components.rememberSnackbarEffectHandler
import com.alterjuice.test.audiobook.ui.components.rememberVisibilityEffectHandler
import com.alterjuice.test.audiobook.ui.utils.BaseSideEffect
import com.alterjuice.test.audiobook.ui.utils.EffectsCollector
import com.alterjuice.test.audiobook.ui.utils.UnhandledEffectStrategyThrowException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Composable
fun AudioBookPlayerScreen(
    modifier: Modifier,
    vm: AudioBookPlayerViewModel = hiltViewModel(),
) = AudioBookPlayerScreen(
    modifier = modifier,
    playerState = vm.state.collectAsStateWithLifecycle(),
    effects = vm.effect,
    onEvent = vm::onEvent
)

@Composable
fun AudioBookPlayerScreen(
    modifier: Modifier,
    playerState: State<AudioBookPlayerState>,
    effects: Flow<BaseSideEffect>,
    onEvent: (PlayerEvent) -> Unit,
) {
    val availableContentScales = remember {
         mutableStateListOf(AudioPlayerContents.AUDIO_CONTROLS, AudioPlayerContents.TEXT)
    }
    val contentState = remember { mutableStateOf(AudioPlayerContents.AUDIO_CONTROLS) }

    val (isSpeedSheetVisible, speedPickerHandler) = rememberVisibilityEffectHandler<PlayerSideEffects.UpdateSpeedPicker>(
        key = true,
        initialValue = false,
        isVisible = PlayerSideEffects.UpdateSpeedPicker::isVisible,
    )

    val (isChapterPickerVisible, chapterPickerHandler) = rememberVisibilityEffectHandler<PlayerSideEffects.UpdateChapterPicker>(
        key = true,
        initialValue = false,
        isVisible = PlayerSideEffects.UpdateChapterPicker::isVisible,
    )
    val snackbarHostState = remember { SnackbarHostState() }
    val snackbarMessageEffectHandler = rememberSnackbarEffectHandler(snackbarHostState)

    EffectsCollector(
        effects,
        speedPickerHandler,
        chapterPickerHandler,
        snackbarMessageEffectHandler,
        strategy = if (BuildConfig.DEBUG) UnhandledEffectStrategyThrowException else UnhandledEffectStrategyThrowException
    )

    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        HorizontallyAnimatedContent(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(padding),
            targetState = contentState.value,
            stateToOrderIndex = { availableContentScales.indexOf(it) },
            contentAlignment = Alignment.Center,
        ) { target ->
            when (target) {
                AudioPlayerContents.TEXT -> {
                    /* Skip Implementation of Text tab since main task was to recreate player-screen */
                }

                AudioPlayerContents.AUDIO_CONTROLS -> {
                    AudioBookPlayerControlsContent(
                        modifier = Modifier.fillMaxSize(),
                        playerState = playerState,
                        onEvent = onEvent
                    )
                }
            }
        }

        if (isSpeedSheetVisible.value) {
            SpeedSelectionBottomSheet(
                availableSpeeds = remember {
                    listOf(0.75f, 1.0f, 1.25f, 1.5f, 2.0f)
                },
                currentSpeed = playerState.value.playbackSpeed,
                onSpeedSelected = { newSpeed ->
                    onEvent(PlayerEvent.SetSpeed(newSpeed))
                },
                onDismiss = {
                    onEvent(PlayerEvent.HideSpeedPicker)
                }
            )
        }
        if (isChapterPickerVisible.value) run {
            val chapters = playerState.value.book?.chapters?: return@run

            ChapterSelectionBottomSheet(
                chapters = chapters,
                currentChapterIndex = playerState.value.currentChapter?.order,
                onChapterSelected = {
                    onEvent(PlayerEvent.ToChapter(it.order))
                },
                onDismiss = {
                    onEvent(PlayerEvent.HideChapterPicker)
                }
            )
        }
    }
}

@Composable
fun AudioBookPlayerControlsContent(
    modifier: Modifier,
    playerState: State<AudioBookPlayerState>,
    onEvent: (PlayerEvent) -> Unit,
) {

    Column(
        modifier = modifier.padding(
            start = 16.dp, end = 16.dp, top = 32.dp, bottom = 120.dp
        ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BookCover(
            imageUrl = playerState.value.bookCoverUrl,
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .aspectRatio(0.7f)
                .clip(MaterialTheme.shapes.medium)
        )
        Spacer(Modifier.height(32.dp))

        ChapterInfo(
            modifier = Modifier.fillMaxWidth(),
            currentChapter = playerState.value.currentChapter,
            totalChapters = playerState.value.book?.chapters?.size?: 0,
            onClick = { onEvent(PlayerEvent.ShowChapterPicker) }
        )

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            AudioPlayerSlider(
                modifier = Modifier.fillMaxWidth(),
                currentPosition = playerState.value.currentPositionMs,
                totalDuration = playerState.value.totalDurationMs,
                onSeek = { positionMs ->
                    onEvent(PlayerEvent.Seek.To(positionMs))
                }
            )
            Spacer(Modifier.height(24.dp))
            SpeedButton(
                modifier = Modifier,
                speed = playerState.value.playbackSpeed,
                onClick = { onEvent(PlayerEvent.ShowSpeedPicker) }
            )
            Spacer(Modifier.height(40.dp))
            AudioPlaybackControls(
                isPlaying = playerState.value.isPlaying,
                onPlayClick = { onEvent(PlayerEvent.Play) },
                onPauseClick = { onEvent(PlayerEvent.Pause) },
                onSeekBackwardClick = { onEvent(PlayerEvent.Seek.Backward) },
                onSeekForwardClick = { onEvent(PlayerEvent.Seek.Forward) },
                onPreviousClick = { onEvent(PlayerEvent.ToPrevious) },
                onNextClick = { onEvent(PlayerEvent.ToNext) }
            )
        }
    }
}




@Composable
@Preview
private fun AudioBookPlayerScreenPreview() {
    AudioBookPlayerScreen(
        modifier = Modifier.fillMaxSize(),
        playerState = remember { mutableStateOf(AudioBookPlayerState()) },
        effects = remember { emptyFlow() },
        onEvent = {}
    )
}