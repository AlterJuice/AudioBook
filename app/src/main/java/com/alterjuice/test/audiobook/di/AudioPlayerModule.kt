package com.alterjuice.test.audiobook.di

import com.alterjuice.test.audiobook.controller.AudioBookPlayerControllerImpl
import com.alterjuice.test.audiobook.book_player.model.AudioBookPlayerController
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
interface IAudioModule {

    @Binds
    fun bindsAudioBookPlayerController(impl: AudioBookPlayerControllerImpl): AudioBookPlayerController
}