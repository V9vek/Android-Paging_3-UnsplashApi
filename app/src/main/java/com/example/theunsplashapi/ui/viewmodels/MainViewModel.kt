package com.example.theunsplashapi.ui.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.example.theunsplashapi.repository.MainRepository

class MainViewModel
@ViewModelInject
constructor(
    private val mainRepository: MainRepository
) : ViewModel() {

    private val latestPhoto = mainRepository.getLatestPhotos(10)
        .cachedIn(viewModelScope)
        .asLiveData()

    private val searchQuery = MutableLiveData<String>("")

    val latestAndSearchPhoto = searchQuery.switchMap { query ->
        if (query.isNullOrEmpty()) {
            latestPhoto
        } else {
            mainRepository.getSearchPhotos(query, 10).cachedIn(viewModelScope).asLiveData()
        }
    }

    fun setQuery(query: String) {
        if (searchQuery.value == query) return
        searchQuery.value = query
    }
}