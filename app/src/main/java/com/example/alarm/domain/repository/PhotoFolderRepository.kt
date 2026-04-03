package com.example.alarm.domain.repository

import android.net.Uri
import com.example.alarm.domain.entity.PhotoFolder
import com.example.alarm.domain.exceptions.CreatePhotoUriException
import com.example.alarm.domain.exceptions.OpenPhotoOutputStreamException
import com.example.alarm.domain.exceptions.PhotoFolderNotSelectedException
import kotlinx.coroutines.flow.Flow
import java.io.OutputStream

interface PhotoFolderRepository {
    fun observePhotoFolder(): Flow<PhotoFolder?>

    suspend fun savePhotoFolder(photoFolder: PhotoFolder)

    @Throws(
        PhotoFolderNotSelectedException::class,
        CreatePhotoUriException::class
    )
    suspend fun createPhotoUri(displayName: String): Uri

    @Throws(OpenPhotoOutputStreamException::class)
    suspend fun getOutputStream(uri: Uri): OutputStream

    suspend fun deletePhoto(uri: Uri)
}
