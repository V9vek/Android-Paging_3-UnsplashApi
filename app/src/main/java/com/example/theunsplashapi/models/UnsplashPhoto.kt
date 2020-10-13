package com.example.theunsplashapi.models

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "photos")
@Parcelize
data class UnsplashPhoto(
    @PrimaryKey
    val id: String,
    val description: String,
    @SerializedName("urls")
    @Embedded
    val imageUrl: PhotoImageUrl,
    @Embedded
    val user: User
) : Parcelable

@Parcelize
data class PhotoImageUrl(
    @SerializedName("regular" /*, alternate = ["medium"]*/)
    val regularImageUrl: String
) : Parcelable

@Parcelize
data class User(
    val name: String,
    @Embedded
    @SerializedName("profile_image")
    val profileImage: ProfileImageUrl
) : Parcelable

@Parcelize
data class ProfileImageUrl(
    @SerializedName("medium")
    val mediumImageUrl: String
) : Parcelable
