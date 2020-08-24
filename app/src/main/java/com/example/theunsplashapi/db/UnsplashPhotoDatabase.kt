package com.example.theunsplashapi.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.theunsplashapi.models.UnsplashPhoto

@Database(
    entities = [UnsplashPhoto::class],
    version = 1,
    exportSchema = false
)
abstract class UnsplashPhotoDatabase : RoomDatabase() {

    abstract fun getUnsplashPhotoDao(): UnsplashPhotoDao
}