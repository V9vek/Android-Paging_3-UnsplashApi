package com.example.theunsplashapi.datasource

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.example.theunsplashapi.db.UnsplashPhotoDatabase
import com.example.theunsplashapi.models.UnsplashPhoto
import com.example.theunsplashapi.retrofit.UnsplashRetrofit
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class UnsplashPhotoRemoteMediator(
    private val retrofit: UnsplashRetrofit,
    private val photoDatabase: UnsplashPhotoDatabase
) : RemoteMediator<Int, UnsplashPhoto>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, UnsplashPhoto>
    ): MediatorResult {

        try {


        } catch (e: IOException) {
            return MediatorResult.Error(e)
        } catch (e: HttpException) {
            return MediatorResult.Error(e)
        }
    }
}