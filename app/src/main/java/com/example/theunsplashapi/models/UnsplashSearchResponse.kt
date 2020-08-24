package com.example.theunsplashapi.models

import com.google.gson.annotations.SerializedName

data class UnsplashSearchResponse(
    @SerializedName("results")
    val result: List<UnsplashPhoto>
)