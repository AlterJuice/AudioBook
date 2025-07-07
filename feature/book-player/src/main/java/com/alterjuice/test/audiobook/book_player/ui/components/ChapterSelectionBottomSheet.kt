package com.alterjuice.test.audiobook.book_player.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alterjuice.test.audiobook.domain.model.Chapter


@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ChapterSelectionBottomSheet(
    chapters: List<Chapter>,
    currentChapterIndex: Int?,
    onChapterSelected: (Chapter) -> Unit,
    onDismiss: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(
                items = chapters,
                key = { it.id }
            ) { chapter ->
                val buttonColors = ButtonDefaults.textButtonColors(
                    contentColor = if (chapter.order == currentChapterIndex)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.onSurface,
                )
                TextButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        onChapterSelected(chapter)
                        onDismiss()
                    },
                    colors = buttonColors
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = chapter.title,
                        textAlign = TextAlign.Start
                    )
                }
            }
        }
    }
}

@Composable
@Preview
private fun ChapterSelectionBottomSheetPreview() {
    ChapterSelectionBottomSheet(
        chapters = listOf(),
        currentChapterIndex = 0,
        onChapterSelected = { },
        onDismiss = { }
    )
}