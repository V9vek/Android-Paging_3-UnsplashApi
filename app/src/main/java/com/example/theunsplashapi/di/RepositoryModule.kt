package com.example.theunsplashapi.di

import com.example.theunsplashapi.db.UnsplashPhotoDao
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
        dao: UnsplashPhotoDao
    ) = MainRepository(retrofit, dao)
}