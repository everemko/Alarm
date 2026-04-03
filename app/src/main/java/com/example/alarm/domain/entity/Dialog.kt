package com.example.alarm.domain.entity

sealed interface Dialog {
    data object CameraScreenPermissionDeniedForever : Dialog
    data object CameraScreenPermissionsLost : Dialog
    data object CameraLocationUnavailable : Dialog
    data object CameraPreparePhotoFailed : Dialog
    data object CameraCaptureFailed : Dialog
    data object CameraPhotoSaved : Dialog
}
