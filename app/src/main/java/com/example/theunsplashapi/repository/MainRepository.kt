package com.example.theunsplashapi.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.theunsplashapi.datasource.UnsplashPhotoPagingSource
import com.example.theunsplashapi.datasource.UnsplashSearchPhotoPagingSource
import com.example.theunsplashapi.db.UnsplashPhotoDao
import com.example.theunsplashapi.retrofit.UnsplashRetrofit

class MainRepository
constructor(
    private val retrofit: UnsplashRetrofit,
    private val dao: UnsplashPhotoDao
) {

    fun getLatestPhotos(itemsPerPage: Int) =
        Pager(
            config = PagingConfig(pageSize = itemsPerPage)
        ) {
            UnsplashPhotoPagingSource(retrofit)
        }.flow

    fun getSearchPhotos(query: String, itemsPerPage: Int) =
        Pager(
            config = PagingConfig(pageSize = itemsPerPage)
        ) {
            UnsplashSearchPhotoPagingSource(retrofit, query)
        }.flow
}