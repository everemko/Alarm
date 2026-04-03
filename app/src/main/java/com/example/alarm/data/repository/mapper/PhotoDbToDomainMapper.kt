package com.example.alarm.data.repository.mapper

import android.net.Uri
import com.example.alarm.domain.entity.Coordinates
import com.example.alarm.domain.entity.Photo
import com.example.alarm.data.database.entity.Photo as PhotoDbEntity
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject

class PhotoDbToDomainMapper @Inject constructor() {
    fun map(source: PhotoDbEntity): Photo {
        val id = source.externalId.ifBlank { "photo_${source.id}" }

        val date = LocalDateTime.ofInstant(
            Instant.ofEpochMilli(source.createdAtMillis),
            ZoneId.systemDefault()
        )

        val coordinates = if (source.latitude != null && source.longitude != null) {
            Coordinates(
                latitude = source.latitude,
                longitude = source.longitude
            )
        } else {
            null
        }

        return Photo(
            id = id,
            name = source.name,
            uri = Uri.parse(source.uri),
            date = date,
            coordinates = coordinates
        )
    }
}
