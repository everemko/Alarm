package com.example.alarm.ui.photos

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun PhotosRoute(
    viewModel: PhotosViewModel = hiltViewModel()
) {
    val photos by viewModel.photos.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    PhotosScreen(
        photos = photos,
        searchQuery = searchQuery,
        onSearchQueryChange = viewModel::onSearchQueryChange,
        onBack = viewModel::onBackClick,
        onPhotoClick = viewModel::onPhotoClick
    )
}
