package com.example.theunsplashapi.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.theunsplashapi.models.UnsplashPhoto

@Dao
interface UnsplashPhotoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllPhotos(photos: List<UnsplashPhoto>)

    @Query("SELECT * FROM photos")
    fun getAllPhotos(): PagingSource<Int, UnsplashPhoto>

    @Query("DELETE FROM photos")
    suspend fun clearPhotos()
}