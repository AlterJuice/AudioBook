package com.alterjuice.test.audiobook.book_player.ui.components

import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview


@Composable
internal fun SpeedButton(
    modifier: Modifier = Modifier,
    speed: Float,
    onClick: () -> Unit,
) {
    FilledTonalButton(
        onClick = onClick,
        modifier = modifier,
        shape = MaterialTheme.shapes.small,
    ) {
        Text("${speed}x speed")
    }
}

@Composable
@Preview
private fun SpeedButtonPreview() {
    SpeedButton(
        modifier = Modifier,
        speed = 1.5f,
        onClick = {}
    )
}