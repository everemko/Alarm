package com.example.alarm.ui.photo

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun PhotoDetailsRoute(
    viewModel: PhotoDetailsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    PhotoDetailsScreen(
        uiState = uiState,
        onBack = viewModel::onBackClick
    )
}
