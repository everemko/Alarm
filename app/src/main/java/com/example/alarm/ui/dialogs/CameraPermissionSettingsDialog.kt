package com.example.alarm.ui.dialogs

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alarm.R
import com.example.alarm.domain.entity.Dialog
import com.example.alarm.domain.entity.Route
import com.example.alarm.domain.repository.DialogFlow
import com.example.alarm.domain.repository.NavigationFlow
import com.example.alarm.domain.repository.PermissionRepository
import com.example.alarm.ui.theme.AlarmTheme
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import kotlinx.coroutines.launch

@Composable
fun CameraPermissionSettingsDialogRoute(
    dialog: Dialog,
    viewModel: CameraPermissionSettingsDialogViewModel = hiltViewModel()
) {
    val messageRes = when (dialog) {
        Dialog.CameraScreenPermissionDeniedForever ->
            R.string.camera_screen_permission_denied_forever_message

        Dialog.CameraScreenPermissionsLost ->
            R.string.camera_permissions_lost_message

        else -> error("Unsupported dialog: $dialog")
    }

    CameraPermissionSettingsDialog(
        messageRes = messageRes,
        onDismiss = viewModel::onDismiss,
        onOpenSettings = viewModel::onOpenSettingsClick
    )
}

@Composable
private fun CameraPermissionSettingsDialog(
    messageRes: Int,
    onDismiss: () -> Unit,
    onOpenSettings: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.camera_dialog_error_title)) },
        text = { Text(stringResource(messageRes)) },
        confirmButton = {
            TextButton(onClick = onOpenSettings) {
                Text(stringResource(R.string.common_open_settings))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.common_back))
            }
        }
    )
}

@Preview(name = "Camera Permission Denied Forever", showBackground = true)
@Composable
private fun CameraPermissionDeniedForeverDialogPreview() {
    AlarmTheme {
        CameraPermissionSettingsDialog(
            messageRes = R.string.camera_screen_permission_denied_forever_message,
            onDismiss = {},
            onOpenSettings = {}
        )
    }
}

@Preview(name = "Camera Permissions Lost", showBackground = true)
@Composable
private fun CameraPermissionsLostDialogPreview() {
    AlarmTheme {
        CameraPermissionSettingsDialog(
            messageRes = R.string.camera_permissions_lost_message,
            onDismiss = {},
            onOpenSettings = {}
        )
    }
}

@HiltViewModel
class CameraPermissionSettingsDialogViewModel @Inject constructor(
    private val dialogFlow: DialogFlow,
    private val navigationFlow: NavigationFlow,
    private val permissionRepository: PermissionRepository
) : ViewModel() {

    fun onDismiss() {
        dialogFlow.dismiss()
        navigationFlow.navigate(Route.Back)
    }

    fun onOpenSettingsClick() = viewModelScope.launch(Dispatchers.IO) {
        dialogFlow.dismiss()
        permissionRepository.openAppSettings()
        navigationFlow.navigate(Route.Back)
    }
}
