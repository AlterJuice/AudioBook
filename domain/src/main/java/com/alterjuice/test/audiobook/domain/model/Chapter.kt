package com.alterjuice.test.audiobook.domain.model

data class Chapter(
    val id: String,
    val order: Int,
    val title: String,
    val audioUrl: String
)
