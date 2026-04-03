package com.example.alarm.data.repository

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.core.content.ContextCompat
import com.example.alarm.data.system.PermissionManager
import com.example.alarm.domain.entity.PermissionStatus
import com.example.alarm.domain.repository.PermissionRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PermissionRepositoryImpl @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val permissionManager: PermissionManager
) : PermissionRepository {

    override fun hasCameraPermission(): Boolean = hasPermission(Manifest.permission.CAMERA)

    override fun hasLocationPermission(): Boolean {
        return hasFineLocationPermission() || hasCoarseLocationPermission()
    }

    override fun hasFineLocationPermission(): Boolean {
        return hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    override suspend fun requestCameraScreenPermission(): PermissionStatus {
        if (hasCameraPermission() && hasLocationPermission()) {
            return PermissionStatus.Granted
        }

        return permissionManager.requestPermissions(CAMERA_SCREEN_PERMISSIONS) { grantResults ->
            grantResults[Manifest.permission.CAMERA] == true &&
                (
                    grantResults[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                        grantResults[Manifest.permission.ACCESS_COARSE_LOCATION] == true
                    )
        }
    }

    override suspend fun requestLocationPermission(): PermissionStatus {
        if (hasLocationPermission()) return PermissionStatus.Granted

        return permissionManager.requestPermissions(LOCATION_PERMISSIONS) { grantResults ->
            grantResults[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                    grantResults[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        }
    }

    override suspend fun openAppSettings() = withContext(Dispatchers.Main.immediate) {
        context.startActivity(
            Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", context.packageName, null)
            ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )
    }

    private fun hasCoarseLocationPermission(): Boolean {
        return hasPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
    }

    private fun hasPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private companion object {
        val CAMERA_SCREEN_PERMISSIONS = listOf(
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        val LOCATION_PERMISSIONS = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }
}
