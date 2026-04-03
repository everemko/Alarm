package com.example.alarm.ui.camera

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alarm.domain.entity.Coordinates
import com.example.alarm.domain.entity.Dialog
import com.example.alarm.domain.entity.PermissionStatus
import com.example.alarm.domain.entity.Photo
import com.example.alarm.domain.entity.Route
import com.example.alarm.domain.exceptions.CapturePhotoException
import com.example.alarm.domain.exceptions.CreatePhotoUriException
import com.example.alarm.domain.exceptions.OpenPhotoOutputStreamException
import com.example.alarm.domain.exceptions.PhotoFolderNotSelectedException
import com.example.alarm.domain.repository.DialogFlow
import com.example.alarm.domain.repository.LocationRepository
import com.example.alarm.domain.repository.NavigationFlow
import com.example.alarm.domain.repository.PermissionRepository
import com.example.alarm.domain.repository.PhotoFileNameRepository
import com.example.alarm.domain.repository.PhotoFolderRepository
import com.example.alarm.domain.repository.PhotoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.SharingStarted.Companion.Eagerly
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import java.time.LocalDateTime
import javax.inject.Inject


@HiltViewModel
class CameraCaptureViewModel @Inject constructor(
    private val photoFolderRepository: PhotoFolderRepository,
    private val photoRepository: PhotoRepository,
    private val locationRepository: LocationRepository,
    private val dialogFlow: DialogFlow,
    private val navigationFlow: NavigationFlow,
    private val permissionRepository: PermissionRepository,
    private val photoFileNameRepository: PhotoFileNameRepository,
) : ViewModel() {

    private lateinit var latestCoordinates: SharedFlow<Coordinates>

    val isFolderSelected: StateFlow<Boolean> = photoFolderRepository.observePhotoFolder()
        .map { folder -> folder != null }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
            initialValue = false
        )

    private val _hasAllPermissions = MutableStateFlow(
        permissionRepository.hasCameraPermission() && permissionRepository.hasLocationPermission()
    )
    val hasAllPermissions: StateFlow<Boolean> = _hasAllPermissions.asStateFlow()

    private val _isSaving = MutableStateFlow(false)
    val isSaving: StateFlow<Boolean> = _isSaving.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val permissionRequestResult = permissionRepository.requestCameraScreenPermission()

            when (permissionRequestResult) {
                PermissionStatus.Granted -> {
                    _hasAllPermissions.value = true
                }

                PermissionStatus.Rejected -> {
                    navigationFlow.navigate(Route.Back)
                    return@launch
                }

                PermissionStatus.RejectedForever -> {
                    dialogFlow.show(Dialog.CameraScreenPermissionDeniedForever)
                    return@launch
                }
            }

            latestCoordinates = locationRepository.observeLocation()
                .shareIn(
                    scope = viewModelScope,
                    started = Eagerly,
                    replay = 1
                )
        }
    }

    fun onBackClick() {
        navigationFlow.navigate(Route.Back)
    }

    fun onOpenFolderClick() {
        navigationFlow.navigate(Route.Folder)
    }

    fun onCaptureClicked(pictureCapturer: PictureCapturer) = viewModelScope.launch(Dispatchers.IO) {
        if (_isSaving.value) return@launch
        _isSaving.value = true

        try {
            if (!permissionRepository.hasLocationPermission()) {
                dialogFlow.show(Dialog.CameraScreenPermissionsLost)
                return@launch
            }

            val date = LocalDateTime.now()
            val name = photoFileNameRepository.create(date)
            val uri = try {
                photoFolderRepository.createPhotoUri(name)
            } catch (_: PhotoFolderNotSelectedException) {
                dialogFlow.show(Dialog.CameraPreparePhotoFailed)
                return@launch
            } catch (_: CreatePhotoUriException) {
                dialogFlow.show(Dialog.CameraPreparePhotoFailed)
                return@launch
            }

            val stream = try {
                photoFolderRepository.getOutputStream(uri)
            } catch (_: OpenPhotoOutputStreamException) {
                photoFolderRepository.deletePhoto(uri)
                dialogFlow.show(Dialog.CameraPreparePhotoFailed)
                return@launch
            }

            try {
                pictureCapturer.capture(stream)
            } catch (_: CapturePhotoException) {
                photoFolderRepository.deletePhoto(uri)
                dialogFlow.show(Dialog.CameraCaptureFailed)
                return@launch
            }

            val coordinates = async {
                withTimeoutOrNull(WAIT_LOCATION_TIMEOUT_MILLIS) {
                    latestCoordinates.firstOrNull()
                }
            }.await()
            if (coordinates == null) {
                dialogFlow.show(Dialog.CameraLocationUnavailable)
                return@launch
            }

            val photo = Photo(
                name = name,
                uri = uri,
                date = date,
                coordinates = coordinates
            )
            try {
                photoRepository.savePhoto(photo)
                dialogFlow.show(Dialog.CameraPhotoSaved)
            } catch (_: Exception) {
                photoFolderRepository.deletePhoto(photo.uri)
                dialogFlow.show(Dialog.CameraCaptureFailed)
            }
        } finally {
            _isSaving.value = false
        }
    }

    private companion object {
        const val WAIT_LOCATION_TIMEOUT_MILLIS = 3_000L
    }
}
