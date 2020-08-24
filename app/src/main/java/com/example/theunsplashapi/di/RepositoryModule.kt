package com.example.theunsplashapi.di

import com.example.theunsplashapi.db.UnsplashPhotoDatabase
import com.example.theunsplashapi.repository.MainRepository
import com.example.theunsplashapi.retrofit.UnsplashRetrofit
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideMainRepository(
        retrofit: UnsplashRetrofit,
        db: UnsplashPhotoDatabase
    ) = MainRepository(retrofit, db)
}