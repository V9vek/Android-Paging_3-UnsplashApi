package com.example.theunsplashapi.datasource

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.theunsplashapi.db.UnsplashPhotoDatabase
import com.example.theunsplashapi.models.RemoteKeys
import com.example.theunsplashapi.models.UnsplashPhoto
import com.example.theunsplashapi.retrofit.UnsplashRetrofit
import retrofit2.HttpException
import java.io.IOException
import java.io.InvalidObjectException

private const val STARTING_PAGE_INDEX = 1

@OptIn(ExperimentalPagingApi::class)
class UnsplashPhotoRemoteMediator(
    private val retrofit: UnsplashRetrofit,
    private val photoDatabase: UnsplashPhotoDatabase
) : RemoteMediator<Int, UnsplashPhoto>() {

    private val photoDao = photoDatabase.getUnsplashPhotoDao()
    private val remoteKeysDao = photoDatabase.getRemoteKeysDao()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, UnsplashPhoto>
    ): MediatorResult {

        try {
            val page = when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.nextKey?.minus(1) ?: STARTING_PAGE_INDEX            // currentPageKey = nextPageKey - 1
                }
                LoadType.PREPEND -> {
                    val remoteKeys = getRemoteKeyForFirstItem(state)
                    if (remoteKeys == null) {
                        throw InvalidObjectException("Remote key should not be null for $loadType")
                    }

                    val prevKey = remoteKeys.prevKey
                    if (prevKey == null) {
                        return MediatorResult.Success(endOfPaginationReached = true)
                    }
                    remoteKeys.prevKey
                }
                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state)
                    if (remoteKeys?.nextKey == null) {
                        throw InvalidObjectException("Remote key should not be null for $loadType")
                    }
                    remoteKeys.nextKey
                }
            }

            val response = retrofit.getLatestPhotos(page, state.config.pageSize)
            val photos = response.body()!!

            val endOfPaginationReached = photos.isEmpty()

            photoDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    photoDao.clearPhotos()
                    remoteKeysDao.clearRemoteKeys()
                }

                val prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = photos.map {
                    RemoteKeys(photoId = it.id, prevKey = prevKey, nextKey = nextKey)
                }

                photoDao.insertAllPhotos(photos)
                remoteKeysDao.insertAll(keys)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)

        } catch (e: IOException) {
            return MediatorResult.Error(e)
        } catch (e: HttpException) {
            return MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, UnsplashPhoto>): RemoteKeys? {
        // Get the last page that was retrieved, that contained items.

        val lastPage = state.pages.lastOrNull {
            it.data.isNotEmpty()            // returns null if last page data is empty else last page's data(list)
        }

        // From that last page, get the last item

        return lastPage?.data?.lastOrNull()?.let {
            remoteKeysDao.remoteKeysPhotoId(it.id)
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, UnsplashPhoto>): RemoteKeys? {
        // Get the first page that was retrieved, that contained items.

        val firstPage = state.pages.firstOrNull() {
            it.data.isNotEmpty()
        }

        // From that first page, get the first item
        return firstPage?.data?.firstOrNull()?.let {
            remoteKeysDao.remoteKeysPhotoId(it.id)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, UnsplashPhoto>): RemoteKeys? {
        // The paging library is trying to load data after the anchor position
        // Get the item closest to the anchor position

        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let {
                remoteKeysDao.remoteKeysPhotoId(it)
            }
        }
    }
}