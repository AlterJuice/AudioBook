package com.alterjuice.test.audiobook.book_player.utils

import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.alterjuice.test.audiobook.domain.model.Book
import androidx.core.net.toUri
import com.alterjuice.test.audiobook.domain.model.Chapter


fun Book.toSortedMediaItems(): List<MediaItem> {
    return this.chapters.sortedBy {
        it.order
    }.mapIndexed { index, chapter ->
        chapter.toMediaItemOfBook(this)
    }
}

fun Chapter.toMediaItemOfBook(
    book: Book,
): MediaItem {
    val metadata = MediaMetadata.Builder()
        .setTitle(this.title)
        .setArtist(book.author)
        .setAlbumTitle(this.title)
        .setArtworkUri(book.coverArtUrl.toUri())
        .setTrackNumber(this.order)
        .setTotalTrackCount(book.chapters.size)
        .build()

    return MediaItem.Builder()
        .setUri(this.audioUrl)
        .setMediaId(this.id)
        .setMediaMetadata(metadata)
        .build()
}