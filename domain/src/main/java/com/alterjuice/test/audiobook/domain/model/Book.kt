package com.alterjuice.test.audiobook.domain.model

import kotlinx.collections.immutable.ImmutableList

data class Book(
    val id: String,
    val title: String,
    val author: String,
    val coverArtUrl: String,
    val description: String,
    val language: String,
    val chapters: ImmutableList<Chapter>,
)

