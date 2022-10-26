package com.appsfactory.test.di

import com.appsfactory.test.data.local.repository.LocalRepositoryImpl
import com.appsfactory.test.data.remote.repository.LastFMRepositoryImpl
import com.appsfactory.test.domain.repository.local.LocalRepository
import com.appsfactory.test.domain.repository.remote.LastFMRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindLastFMRepository(
        lastFMRepositoryImpl: LastFMRepositoryImpl
    ): LastFMRepository

    @Binds
    @Singleton
    abstract fun provideRoomRepository(
        roomRepositoryImpl: LocalRepositoryImpl
    ): LocalRepository
}