package com.example.theunsplashapi.retrofit

import com.example.theunsplashapi.models.UnsplashPhoto
import com.example.theunsplashapi.models.UnsplashSearchResponse
import com.example.theunsplashapi.utils.Constants.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface UnsplashRetrofit {

    @Headers("Authorization: Client-ID $API_KEY")
    @GET("photos")
    suspend fun getLatestPhotos(
        @Query("page") page: Int,
        @Query("per_page") itemsPerPage: Int
    ): Response<List<UnsplashPhoto>>

    @Headers("Authorization: Client-ID $API_KEY")
    @GET("search/photos")
    suspend fun getSearchPhotos(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("per_page") itemsPerPage: Int
    ): Response<UnsplashSearchResponse>
}