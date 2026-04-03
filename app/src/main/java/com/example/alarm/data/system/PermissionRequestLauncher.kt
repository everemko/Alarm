package com.example.alarm.data.system

import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.launch
import javax.inject.Inject

@ActivityScoped
class PermissionRequestLauncher @Inject constructor(
    private val activity: ComponentActivity,
    private val permissionManager: PermissionManager
) {

    private val requestPermissionsLauncher = activity.registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { grantResults ->
        permissionManager.onPermissionResult(
            grantResults = grantResults,
            shouldShowRationale = { permission ->
                ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)
            }
        )
    }

    init {
        activity.lifecycleScope.launch {
            activity.repeatOnLifecycle(Lifecycle.State.STARTED) {
                permissionManager.requests.collect { permissions ->
                    requestPermissionsLauncher.launch(permissions.toTypedArray())
                }
            }
        }
    }
}
