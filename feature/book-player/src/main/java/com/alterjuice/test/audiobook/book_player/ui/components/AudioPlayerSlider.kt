package com.alterjuice.test.audiobook.book_player.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.util.concurrent.TimeUnit


private fun formatTime(timeMs: Long): String {
    val minutes = TimeUnit.MILLISECONDS.toMinutes(timeMs) % 60
    val seconds = TimeUnit.MILLISECONDS.toSeconds(timeMs) % 60
    return String.format("%02d:%02d", minutes, seconds)
}

@Composable
internal fun AudioPlayerSlider(
    modifier: Modifier = Modifier,
    currentPosition: Long,
    totalDuration: Long,
    onSeek: (Long) -> Unit,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        var isSeeking by remember { mutableStateOf(false) }
        var sliderPosition by remember { mutableFloatStateOf(0f)}


        val durationRange = remember(totalDuration) {
            0f..(totalDuration.takeIf { it > 0 }?.toFloat() ?: 0f)
        }
        val displayValue = if (isSeeking) sliderPosition else currentPosition.toFloat()
        val displayedPositionTime = if (isSeeking) sliderPosition.toLong() else currentPosition

        Text(
            text = formatTime(displayedPositionTime),
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier
        )

        Slider(
            modifier = Modifier.weight(1f),
            value = displayValue,
            onValueChange = {
                sliderPosition = it
                isSeeking = true
            },
            valueRange = durationRange,
            onValueChangeFinished = {
                onSeek(sliderPosition.toLong())
                isSeeking = false
            }
        )

        val displayTotal = totalDuration.takeIf { it > 0L }?.let(::formatTime) ?: "--:--"
        Text(
            text = displayTotal,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.width(IntrinsicSize.Max)
        )

    }
}


@Composable
@Preview
private fun AudioPlayerSliderPreview() {
    AudioPlayerSlider(
        modifier = Modifier,
        currentPosition = 43L,
        totalDuration = 60L,
        onSeek = {}
    )
}