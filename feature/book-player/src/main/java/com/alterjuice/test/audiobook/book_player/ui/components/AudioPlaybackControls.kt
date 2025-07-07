package com.alterjuice.test.audiobook.book_player.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.alterjuice.test.audiobook.book_player.R
import com.alterjuice.test.audiobook.ui.theme.customScheme


@Composable
internal fun AudioPlaybackControls(
    modifier: Modifier = Modifier,
    isPlaying: Boolean,
    onPreviousClick: () -> Unit,
    onSeekBackwardClick: () -> Unit,
    onPlayClick: () -> Unit,
    onPauseClick: () -> Unit,
    onSeekForwardClick: () -> Unit,
    onNextClick: () -> Unit,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally)
    ) {
        val iconModifier = Modifier//.size(68.dp)
        PlaybackControlButton(
            modifier = iconModifier,
            painter = painterResource(R.drawable.round_skip_previous_24),
            onClick = onPreviousClick
        )
        PlaybackControlButton(
            modifier = iconModifier,
            painter = painterResource(R.drawable.round_replay_5_24),
            onClick = onSeekBackwardClick
        )
        if (isPlaying) {
            PlaybackControlButton(
                modifier = iconModifier,
                painter = painterResource(R.drawable.round_pause_24),
                onClick = onPauseClick
            )
        } else {
            PlaybackControlButton(
                modifier = iconModifier,
                painter = painterResource(R.drawable.round_play_arrow_24),
                onClick = onPlayClick
            )
        }
        PlaybackControlButton(
            modifier = iconModifier,
            painter = painterResource(R.drawable.round_forward_10_24),
            onClick = onSeekForwardClick
        )
        PlaybackControlButton(
            modifier = iconModifier,
            painter = painterResource(R.drawable.round_skip_next_24),
            onClick = onNextClick
        )
    }
}

@Composable
private fun PlaybackControlButton(
    modifier: Modifier,
    painter: Painter,
    iconSize: Dp = 60.dp,
    contentDescription: String? = null,
    onClick: () -> Unit,
) = IconButton(onClick = onClick, modifier = modifier.size(iconSize)) {
    Icon(
        painter = painter,
        contentDescription = contentDescription,
        modifier = Modifier.fillMaxSize(),
        tint = MaterialTheme.customScheme.PlayerTextPrimary

    )
}

@Composable
@Preview
private fun AudioPlaybackControlsPreview() {
    AudioPlaybackControls(
        modifier = Modifier,
        isPlaying = true,
        onPreviousClick = {},
        onSeekBackwardClick = {},
        onPlayClick = {},
        onPauseClick = {},
        onSeekForwardClick = {},
        onNextClick = {},
    )
}