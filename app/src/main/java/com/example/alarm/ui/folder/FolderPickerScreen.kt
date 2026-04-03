package com.example.alarm.ui.folder

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.alarm.R
import com.example.alarm.ui.common.ScreenToolbar
import com.example.alarm.ui.theme.AlarmTheme

@Composable
fun FolderPickerScreen(
    selectedFolderUri: Uri?,
    onFolderSelected: (Uri) -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val folderAccessErrorText = stringResource(R.string.folder_access_error)
    val pickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocumentTree()) { uri ->
        if (uri == null) return@rememberLauncherForActivityResult

        val flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
        runCatching {
            context.contentResolver.takePersistableUriPermission(uri, flags)
            onFolderSelected(uri)
        }.onFailure {
            Toast.makeText(
                context,
                folderAccessErrorText,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    FolderPickerContent(
        selectedFolderUri = selectedFolderUri,
        onBack = onBack,
        onPickFolder = { pickerLauncher.launch(selectedFolderUri) }
    )
}

@Composable
private fun FolderPickerContent(
    selectedFolderUri: Uri?,
    onBack: () -> Unit,
    onPickFolder: () -> Unit
) {
    val selectedFolderLabel = selectedFolderUri?.toString() ?: stringResource(R.string.common_not_selected)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        ScreenToolbar(
            title = stringResource(R.string.screen_folder_title),
            onBack = onBack
        )
        Text(stringResource(R.string.folder_current_folder, selectedFolderLabel))

        Button(
            onClick = onPickFolder,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.folder_pick_button))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FolderPickerScreenPreview() {
    AlarmTheme {
        FolderPickerContent(
            selectedFolderUri = null,
            onBack = {},
            onPickFolder = {}
        )
    }
}
