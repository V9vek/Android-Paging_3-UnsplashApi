package com.example.theunsplashapi.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.theunsplashapi.datasource.UnsplashPhotoRemoteMediator
import com.example.theunsplashapi.datasource.UnsplashSearchPhotoPagingSource
import com.example.theunsplashapi.db.UnsplashPhotoDatabase
import com.example.theunsplashapi.models.UnsplashPhoto
import com.example.theunsplashapi.retrofit.UnsplashRetrofit
import kotlinx.coroutines.flow.Flow

class MainRepository
constructor(
    private val retrofit: UnsplashRetrofit,
    private val photoDatabase: UnsplashPhotoDatabase
) {


    fun getLatestPhotos(itemsPerPage: Int): Flow<PagingData<UnsplashPhoto>> {
        val pagingSourceFactory = { photoDatabase.getUnsplashPhotoDao().getAllPhotos() }

        return Pager(
            config = PagingConfig(pageSize = itemsPerPage),
            remoteMediator = UnsplashPhotoRemoteMediator(retrofit, photoDatabase),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    fun getSearchPhotos(query: String, itemsPerPage: Int) =
        Pager(
            config = PagingConfig(pageSize = itemsPerPage)
        ) {
            UnsplashSearchPhotoPagingSource(retrofit, query)
        }.flow
}