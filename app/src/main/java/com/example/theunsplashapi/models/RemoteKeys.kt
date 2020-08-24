package com.example.theunsplashapi.models

import androidx.room.Entity

@Entity(tableName = "remote_keys")
data class RemoteKeys(
    val photoId: String,
    val prevKey: Int?,
    val nextKey: Int?
)
