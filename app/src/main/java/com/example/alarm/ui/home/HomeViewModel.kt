package com.example.alarm.ui.home

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alarm.domain.entity.Route
import com.example.alarm.domain.repository.NavigationFlow
import com.example.alarm.domain.repository.PhotoFolderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    photoFolderRepository: PhotoFolderRepository,
    private val navigationFlow: NavigationFlow
) : ViewModel() {

    val selectedFolderUri: StateFlow<Uri?> = photoFolderRepository.observePhotoFolder()
        .map { it?.uri }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
            initialValue = null
        )

    fun onCameraClick() {
        navigationFlow.navigate(Route.Camera)
    }

    fun onFolderClick() {
        navigationFlow.navigate(Route.Folder)
    }

    fun onPhotosClick() {
        navigationFlow.navigate(Route.Photos)
    }

    fun onMapClick() {
        navigationFlow.navigate(Route.Map)
    }
}
