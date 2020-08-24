package com.example.theunsplashapi.datasource

import androidx.paging.PagingSource
import com.example.theunsplashapi.models.UnsplashPhoto
import com.example.theunsplashapi.retrofit.UnsplashRetrofit

private const val STARTING_PAGE_INDEX = 1

class UnsplashPhotoPagingSource(
    val retrofit: UnsplashRetrofit
) : PagingSource<Int, UnsplashPhoto>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UnsplashPhoto> {

        val position = params.key ?: STARTING_PAGE_INDEX
        return try {
            val response = retrofit.getLatestPhotos(position, params.loadSize)
            val photos = response.body()!!

            LoadResult.Page(
                data = photos,
                prevKey = if (position == 1) null else position - 1,
                nextKey = if (photos.isEmpty()) null else position + 1
            )

        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}