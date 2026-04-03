package com.example.alarm.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.alarm.data.database.entity.Photo
import kotlinx.coroutines.flow.Flow

@Dao
interface PhotoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(photo: Photo): Long

    @Query("SELECT * FROM photos ORDER BY createdAtMillis DESC")
    fun observeAll(): Flow<List<Photo>>

    @Query("SELECT * FROM photos WHERE externalId = :externalId LIMIT 1")
    suspend fun getByExternalId(externalId: String): Photo?

    @Query("SELECT * FROM photos ORDER BY createdAtMillis DESC")
    suspend fun getAll(): List<Photo>
}
