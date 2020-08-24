package com.example.theunsplashapi.models

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "photos")
data class UnsplashPhoto(
    @PrimaryKey
    val id: String,
    @SerializedName("urls")
    @Embedded
    val imageUrl: PhotoImageUrl,
    @Embedded
    val user: User
)

data class PhotoImageUrl(
    @SerializedName("regular" /*, alternate = ["medium"]*/)
    val regularImageUrl: String
)

data class ProfileImageUrl(
    @SerializedName("medium")
    val mediumImageUrl: String
)

data class User(
    val name: String,
    @Embedded
    @SerializedName("profile_image")
    val profileImage: ProfileImageUrl
)
