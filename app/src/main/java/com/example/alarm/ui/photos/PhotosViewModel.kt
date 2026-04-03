package com.example.alarm.ui.photos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alarm.domain.entity.Photo
import com.example.alarm.domain.entity.Route
import com.example.alarm.domain.repository.NavigationFlow
import com.example.alarm.domain.usecase.ObservePhotosByDateSearchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class PhotosViewModel @Inject constructor(
    observePhotosByDateSearchUseCase: ObservePhotosByDateSearchUseCase,
    private val navigationFlow: NavigationFlow
) : ViewModel() {

    private val searchQueryFlow = MutableStateFlow("")

    val searchQuery: StateFlow<String> = searchQueryFlow.asStateFlow()

    val photos: StateFlow<List<Photo>> = observePhotosByDateSearchUseCase(searchQueryFlow)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
            initialValue = emptyList()
        )

    fun onSearchQueryChange(query: String) {
        searchQueryFlow.value = query
    }

    fun onBackClick() {
        navigationFlow.navigate(Route.Back)
    }

    fun onPhotoClick(photoId: String) {
        navigationFlow.navigate(Route.PhotoDetails(photoId))
    }
}
