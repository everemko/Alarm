package com.example.alarm.ui.dialogs

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.alarm.R
import com.example.alarm.ui.theme.AlarmTheme

@Composable
fun CameraCaptureFailedDialog(
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.camera_dialog_error_title)) },
        text = { Text(stringResource(R.string.camera_capture_failed_message)) },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.common_ok))
            }
        }
    )
}

@Preview(name = "Camera Capture Failed", showBackground = true)
@Composable
private fun CameraCaptureFailedDialogPreview() {
    AlarmTheme {
        CameraCaptureFailedDialog(
            onDismiss = {}
        )
    }
}
