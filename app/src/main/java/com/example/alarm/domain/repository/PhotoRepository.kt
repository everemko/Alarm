package com.example.alarm.domain.repository

import com.example.alarm.domain.entity.Photo
import kotlinx.coroutines.flow.Flow

interface PhotoRepository {
    suspend fun savePhoto(photo: Photo): Long

    suspend fun getPhoto(id: String): Photo?

    suspend fun getPhotos(): List<Photo>
    fun observePhotos(): Flow<List<Photo>>
}
