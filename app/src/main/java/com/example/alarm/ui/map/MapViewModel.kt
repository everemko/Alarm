package com.example.alarm.ui.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alarm.domain.entity.Coordinates
import com.example.alarm.domain.entity.Route
import com.example.alarm.domain.repository.NavigationFlow
import com.example.alarm.domain.repository.PhotoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class MapUiState(
    val coordinates: List<Coordinates>,
    val camera: MapCamera
)

data class MapCamera(
    val latitude: Double,
    val longitude: Double,
    val zoom: Double
)

@HiltViewModel
class MapViewModel @Inject constructor(
    photoRepository: PhotoRepository,
    private val navigationFlow: NavigationFlow
) : ViewModel() {

    val uiState: StateFlow<MapUiState> = photoRepository.observePhotos()
        .map { photos ->
            photos
                .mapNotNull { photo -> photo.coordinates }
                .toUiState()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
            initialValue = MapUiState(
                coordinates = emptyList(),
                camera = defaultCamera()
            )
        )

    fun onBackClick() {
        navigationFlow.navigate(Route.Back)
    }

    private fun List<Coordinates>.toUiState(): MapUiState {
        return MapUiState(
            coordinates = this,
            camera = toCamera()
        )
    }

    private fun List<Coordinates>.toCamera(): MapCamera {
        if (isEmpty()) return defaultCamera()

        return MapCamera(
            latitude = map(Coordinates::latitude).average(),
            longitude = map(Coordinates::longitude).average(),
            zoom = if (size == 1) {
                SINGLE_PHOTO_MAP_ZOOM
            } else {
                MULTI_PHOTO_MAP_ZOOM
            }
        )
    }

    private companion object {
        const val DEFAULT_MAP_LONGITUDE = 27.567444
        const val DEFAULT_MAP_LATITUDE = 53.893009
        const val DEFAULT_EMPTY_MAP_ZOOM = 5.5
        const val SINGLE_PHOTO_MAP_ZOOM = 14.0
        const val MULTI_PHOTO_MAP_ZOOM = 10.0

        fun defaultCamera(): MapCamera {
            return MapCamera(
                latitude = DEFAULT_MAP_LATITUDE,
                longitude = DEFAULT_MAP_LONGITUDE,
                zoom = DEFAULT_EMPTY_MAP_ZOOM
            )
        }
    }
}
