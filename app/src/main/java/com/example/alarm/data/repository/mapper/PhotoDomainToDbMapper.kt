package com.example.alarm.data.repository.mapper

import com.example.alarm.domain.entity.Photo
import com.example.alarm.data.database.entity.Photo as PhotoDbEntity
import java.time.ZoneId
import javax.inject.Inject

class PhotoDomainToDbMapper @Inject constructor() {
    fun map(source: Photo): PhotoDbEntity {
        val epochMillis = source.date
            .atZone(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()

        return PhotoDbEntity(
            externalId = source.id,
            name = source.name,
            uri = source.uri.toString(),
            createdAtMillis = epochMillis,
            latitude = source.coordinates?.latitude,
            longitude = source.coordinates?.longitude
        )
    }
}
