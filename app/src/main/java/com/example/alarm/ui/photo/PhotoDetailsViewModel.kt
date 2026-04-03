package com.example.alarm.ui.photo

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alarm.domain.entity.Photo
import com.example.alarm.domain.entity.Route
import com.example.alarm.domain.repository.NavigationFlow
import com.example.alarm.domain.repository.PhotoRepository
import com.example.alarm.navigation.PHOTO_ID_ARG
import com.example.alarm.navigation.RouteFactory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PhotoDetailsUiState(
    val isLoading: Boolean = true,
    val photo: Photo? = null
)

@HiltViewModel
class PhotoDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val photoRepository: PhotoRepository,
    private val navigationFlow: NavigationFlow
) : ViewModel() {

    private val _uiState = MutableStateFlow(PhotoDetailsUiState())
    val uiState: StateFlow<PhotoDetailsUiState> = _uiState.asStateFlow()

    private val photoId: String? = savedStateHandle.get<String>(PHOTO_ID_ARG)

    init {
        viewModelScope.launch(Dispatchers.IO) {
            if (photoId == null) {
                _uiState.value = PhotoDetailsUiState(isLoading = false)
                return@launch
            }

            _uiState.value = PhotoDetailsUiState(
                isLoading = false,
                photo = photoRepository.getPhoto(photoId)
            )
        }
    }

    fun onBackClick() {
        navigationFlow.navigate(Route.Back)
    }
}
