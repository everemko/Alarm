package com.example.alarm.ui.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun HomeRoute(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val selectedFolderUri by viewModel.selectedFolderUri.collectAsState()

    HomeScreen(
        selectedFolderUri = selectedFolderUri,
        onCameraClick = viewModel::onCameraClick,
        onFolderClick = viewModel::onFolderClick,
        onPhotosClick = viewModel::onPhotosClick,
        onMapClick = viewModel::onMapClick
    )
}
