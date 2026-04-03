package com.example.alarm.ui.camera

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun CameraCaptureRoute(
    viewModel: CameraCaptureViewModel = hiltViewModel()
) {
    val isFolderSelected by viewModel.isFolderSelected.collectAsState()
    val hasAllPermissions by viewModel.hasAllPermissions.collectAsState()
    val isSaving by viewModel.isSaving.collectAsState()

    CameraCaptureScreen(
        isFolderSelected = isFolderSelected,
        hasAllPermissions = hasAllPermissions,
        isSaving = isSaving,
        onCaptureClicked = viewModel::onCaptureClicked,
        onBack = viewModel::onBackClick,
        onOpenFolder = viewModel::onOpenFolderClick
    )
}
