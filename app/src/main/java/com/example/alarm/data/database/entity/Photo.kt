package com.example.alarm.data.database.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "photos",
    indices = [Index(value = ["externalId"], unique = true)]
)
data class Photo(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val externalId: String,
    val name: String,
    val uri: String,
    val createdAtMillis: Long,
    val latitude: Double? = null,
    val longitude: Double? = null
)
