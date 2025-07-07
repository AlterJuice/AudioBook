package com.alterjuice.test.audiobook.book_player.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alterjuice.test.audiobook.domain.model.Chapter
import com.alterjuice.test.audiobook.ui.components.HorizontallyAnimatedContent
import com.alterjuice.test.audiobook.ui.theme.customScheme


@Composable
internal fun ChapterInfo(
    modifier: Modifier = Modifier,
    currentChapter: Chapter?,
    totalChapters: Int,
    onClick: () -> Unit = {},
) {
    Column(
        modifier = modifier.clickable(
            null, null, true, onClick = onClick
        ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        currentChapter?.let {
            Text(
                text = remember(currentChapter) {
                    "KEY POINT ${currentChapter.order.plus(1)} OF $totalChapters".uppercase()
                },
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.customScheme.PlayerTextSecondary
            )
        }
        HorizontallyAnimatedContent(
            modifier = Modifier.heightIn(min = 80.dp),
            targetState = currentChapter,
            stateToOrderIndex = { it?.order ?: 0 },
            contentAlignment = Alignment.Center
        ) { localCurrentChapter ->
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                text = localCurrentChapter?.title ?: "",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.customScheme.PlayerTextPrimary,
                textAlign = TextAlign.Center,
                minLines = 1
            )
        }
    }
}


@Composable
@Preview
private fun ChapterInfoPreview() {
    ChapterInfo(
        modifier = Modifier,
        currentChapter = Chapter(
            order = 0,
            title = "Introduction",
            audioUrl = "https://example.com/audio.mp3",
            id = "intro"
        ),
        totalChapters = 10
    )
}