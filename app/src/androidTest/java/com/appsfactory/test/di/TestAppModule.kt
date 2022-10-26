package com.appsfactory.test.di

import android.app.Application
import androidx.room.Room
import com.appsfactory.test.data.local.room.AlbumDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object TestAppModule {

    @Provides
    @Named("test_db")
    fun provideInMemoryDatabase(
        app: Application
    ): AlbumDatabase {
        return Room.inMemoryDatabaseBuilder(
            app,
            AlbumDatabase::class.java
        ).allowMainThreadQueries().build()
    }
}