package com.example.theunsplashapi.di

import android.content.Context
import androidx.room.Room
import com.example.theunsplashapi.db.UnsplashPhotoDatabase
import com.example.theunsplashapi.utils.Constants.UNSPLASH_PHOTO_DB_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object RoomModule {

    @Singleton
    @Provides
    fun provideUnsplashPhotoDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context,
        UnsplashPhotoDatabase::class.java,
        UNSPLASH_PHOTO_DB_NAME
    ).build()

    @Singleton
    @Provides
    fun provideUnsplashPhotoDao(db: UnsplashPhotoDatabase) = db.getUnsplashPhotoDao()
}