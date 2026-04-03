package com.example.alarm.data.repository

import android.content.Context
import android.net.Uri
import android.provider.DocumentsContract
import androidx.core.net.toUri
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.alarm.domain.entity.PhotoFolder
import com.example.alarm.domain.exceptions.CreatePhotoUriException
import com.example.alarm.domain.exceptions.OpenPhotoOutputStreamException
import com.example.alarm.domain.exceptions.PhotoFolderNotSelectedException
import com.example.alarm.domain.repository.PhotoFolderRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private const val PHOTO_FOLDER_PREFERENCES_NAME = "photo_folder_preferences"
private const val PHOTO_MIME_TYPE = "image/jpeg"
private val Context.photoFolderDataStore by preferencesDataStore(
    name = PHOTO_FOLDER_PREFERENCES_NAME
)

class PhotoFolderRepositoryImpl @Inject constructor(
    @param:ApplicationContext private val context: Context
) : PhotoFolderRepository {

    private companion object {
        val PHOTO_FOLDER_URI_KEY = stringPreferencesKey("photo_folder_uri")
    }

    override fun observePhotoFolder(): Flow<PhotoFolder?> {
        return context.photoFolderDataStore.data.map { preferences ->
            preferences.photoFolderUriOrNull()?.let { uri -> PhotoFolder(uri = uri) }
        }
    }

    override suspend fun savePhotoFolder(photoFolder: PhotoFolder) {
        context.photoFolderDataStore.edit { preferences ->
            preferences[PHOTO_FOLDER_URI_KEY] = photoFolder.uri.toString()
        }
    }

    @Throws(
        PhotoFolderNotSelectedException::class,
        CreatePhotoUriException::class
    )
    override suspend fun createPhotoUri(displayName: String): Uri {
        val selectedFolderUri = context.photoFolderDataStore.data.first().photoFolderUriOrNull()
            ?: throw PhotoFolderNotSelectedException()

        return runCatching {
            DocumentsContract.createDocument(
                context.contentResolver,
                selectedFolderUri.asParentDocumentUri(),
                PHOTO_MIME_TYPE,
                displayName
            )
        }.getOrNull() ?: throw CreatePhotoUriException()
    }

    @Throws(OpenPhotoOutputStreamException::class)
    override suspend fun getOutputStream(uri: Uri): java.io.OutputStream {
        return runCatching {
            context.contentResolver.openOutputStream(uri)
        }.getOrNull() ?: throw OpenPhotoOutputStreamException()
    }

    override suspend fun deletePhoto(uri: Uri) {
        runCatching {
            DocumentsContract.deleteDocument(context.contentResolver, uri)
        }
    }

    private fun Preferences.photoFolderUriOrNull(): Uri? {
        return this[PHOTO_FOLDER_URI_KEY]
            ?.takeUnless { it.isBlank() }
            ?.toUri()
    }

    private fun Uri.asParentDocumentUri(): Uri {
        if (!DocumentsContract.isTreeUri(this)) return this

        return DocumentsContract.buildDocumentUriUsingTree(
            this,
            DocumentsContract.getTreeDocumentId(this)
        )
    }
}
