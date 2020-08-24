package com.example.theunsplashapi.datasource

import androidx.paging.PagingSource
import com.example.theunsplashapi.models.UnsplashPhoto
import com.example.theunsplashapi.models.UnsplashSearchResponse
import com.example.theunsplashapi.retrofit.UnsplashRetrofit

class UnsplashSearchPhotoPagingSource(
    private val retrofit: UnsplashRetrofit,
    private val query: String
) : PagingSource<Int, UnsplashPhoto>(){

    private val initialPage = 1

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UnsplashPhoto> {

        val position = params.key ?: initialPage

        return try {
            val response = retrofit.getSearchPhotos(query, position, params.loadSize)

            response.body()?.let {
                LoadResult.Page(
                    data = it.result,
                    prevKey = if (position == 1) null else position - 1,
                    nextKey = if (it.result.isEmpty()) null else position + 1
                )
            }!!

        }catch (e: Exception){
            LoadResult.Error(e)
        }
    }
}