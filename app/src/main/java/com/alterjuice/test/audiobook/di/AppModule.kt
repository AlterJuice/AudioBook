package com.alterjuice.test.audiobook.di

import com.alterjuice.test.audiobook.errors.GlobalErrorMessagesProvider
import com.alterjuice.test.audiobook.errors.RootAppErrorMessagesProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class AppModule {

    @Provides
    @Singleton
    internal fun provideRootMessagesHandler(): RootAppErrorMessagesProvider {
        return RootAppErrorMessagesProvider.Builder()
            .registryProvider(GlobalErrorMessagesProvider)
            .build()
    }
}