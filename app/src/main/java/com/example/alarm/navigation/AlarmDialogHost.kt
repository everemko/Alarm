package com.example.alarm.navigation

import androidx.compose.runtime.Composable
import com.example.alarm.domain.entity.Dialog
import com.example.alarm.ui.dialogs.CameraCaptureFailedDialog
import com.example.alarm.ui.dialogs.CameraLocationUnavailableDialog
import com.example.alarm.ui.dialogs.CameraPermissionSettingsDialogRoute
import com.example.alarm.ui.dialogs.CameraPhotoSavedDialog
import com.example.alarm.ui.dialogs.CameraPreparePhotoFailedDialog

@Composable
fun AlarmDialogHost(
    dialog: Dialog,
    onDismiss: () -> Unit,
    onBack: () -> Unit
) {
    when (dialog) {
        Dialog.CameraScreenPermissionDeniedForever,
        Dialog.CameraScreenPermissionsLost -> CameraPermissionSettingsDialogRoute(dialog = dialog)

        Dialog.CameraLocationUnavailable -> CameraLocationUnavailableDialog(
            onDismiss = onDismiss
        )

        Dialog.CameraPreparePhotoFailed -> CameraPreparePhotoFailedDialog(
            onDismiss = onDismiss
        )

        Dialog.CameraCaptureFailed -> CameraCaptureFailedDialog(
            onDismiss = onDismiss
        )

        Dialog.CameraPhotoSaved -> CameraPhotoSavedDialog(
            onDismiss = {
                onDismiss()
                onBack()
            }
        )
    }
}
