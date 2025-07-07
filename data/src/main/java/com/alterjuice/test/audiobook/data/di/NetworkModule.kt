package com.alterjuice.test.audiobook.data.di

import com.alterjuice.test.audiobook.data.network.service.BookApiService
import com.alterjuice.test.audiobook.data.network.service.BookApiServiceTestMock
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal interface INetworkModule {

    @Binds
    @Singleton
    fun providesBookApiService(impl: BookApiServiceTestMock): BookApiService
}
