package com.example.alarm.domain.repository

import com.example.alarm.domain.entity.PermissionStatus

interface PermissionRepository {

    fun hasCameraPermission(): Boolean

    fun hasLocationPermission(): Boolean

    fun hasFineLocationPermission(): Boolean

    suspend fun requestCameraScreenPermission(): PermissionStatus

    suspend fun requestLocationPermission(): PermissionStatus

    suspend fun openAppSettings()
}
