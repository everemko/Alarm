package com.example.alarm.ui.home

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.alarm.R
import com.example.alarm.ui.common.ScreenToolbar
import com.example.alarm.ui.theme.AlarmTheme

@Composable
fun HomeScreen(
    selectedFolderUri: Uri?,
    onCameraClick: () -> Unit,
    onFolderClick: () -> Unit,
    onPhotosClick: () -> Unit,
    onMapClick: () -> Unit
) {
    val selectedFolderLabel = selectedFolderUri?.toString() ?: stringResource(R.string.common_not_selected)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        ScreenToolbar(
            title = stringResource(R.string.home_title)
        )
        Text(stringResource(R.string.home_selected_folder, selectedFolderLabel))

        Button(onClick = onCameraClick, modifier = Modifier.fillMaxWidth()) {
            Text(stringResource(R.string.home_open_camera))
        }
        Button(onClick = onFolderClick, modifier = Modifier.fillMaxWidth()) {
            Text(stringResource(R.string.screen_folder_title))
        }
        Button(onClick = onPhotosClick, modifier = Modifier.fillMaxWidth()) {
            Text(stringResource(R.string.screen_photos_title))
        }
        Button(onClick = onMapClick, modifier = Modifier.fillMaxWidth()) {
            Text(stringResource(R.string.screen_map_title))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    AlarmTheme {
        HomeScreen(
            selectedFolderUri = null,
            onCameraClick = {},
            onFolderClick = {},
            onPhotosClick = {},
            onMapClick = {}
        )
    }
}
