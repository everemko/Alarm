package com.example.alarm.ui.folder

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alarm.domain.entity.PhotoFolder
import com.example.alarm.domain.entity.Route
import com.example.alarm.domain.repository.NavigationFlow
import com.example.alarm.domain.repository.PhotoFolderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FolderPickerViewModel @Inject constructor(
    private val photoFolderRepository: PhotoFolderRepository,
    private val navigationFlow: NavigationFlow
) : ViewModel() {

    val selectedFolderUri: StateFlow<Uri?> = photoFolderRepository.observePhotoFolder()
        .map { folder -> folder?.uri }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
            initialValue = null
        )

    fun onBackClick() {
        navigationFlow.navigate(Route.Back)
    }

    fun saveSelectedFolder(uri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            photoFolderRepository.savePhotoFolder(PhotoFolder(uri = uri))
        }
    }
}
