package com.example.alarm.ui.folder

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun FolderPickerRoute(
    viewModel: FolderPickerViewModel = hiltViewModel()
) {
    val selectedFolderUri by viewModel.selectedFolderUri.collectAsState()

    FolderPickerScreen(
        selectedFolderUri = selectedFolderUri,
        onFolderSelected = viewModel::saveSelectedFolder,
        onBack = viewModel::onBackClick
    )
}
