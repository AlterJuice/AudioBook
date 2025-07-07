package com.alterjuice.test.audiobook.book_player.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage

@Composable
internal fun BookCover(imageUrl: String, modifier: Modifier = Modifier) {
    AsyncImage(
        model = imageUrl,
        contentDescription = "Book Cover",
        modifier = modifier,
        contentScale = ContentScale.Crop,
    )
}

@Composable
@Preview
private fun BookCoverPreview() {
    BookCover(
        modifier = Modifier,
        imageUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR7CcLBg5BCQIxvC2JAPSBJqSJxS-4bZbKsiw&s"
    )
}