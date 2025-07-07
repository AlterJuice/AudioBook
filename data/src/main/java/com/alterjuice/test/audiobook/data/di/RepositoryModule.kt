package com.alterjuice.test.audiobook.data.di

import com.alterjuice.test.audiobook.data.repository.BookRepositoryImpl
import com.alterjuice.test.audiobook.domain.repository.BookRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
internal interface IRepositoryModule {

    @Binds
    @Singleton
    fun bindBookRepository(bookRepositoryImpl: BookRepositoryImpl): BookRepository
}

