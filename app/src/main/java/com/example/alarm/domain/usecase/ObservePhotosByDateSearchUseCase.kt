package com.example.alarm.domain.usecase

import com.example.alarm.domain.entity.Photo
import com.example.alarm.domain.repository.PhotoRepository
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class ObservePhotosByDateSearchUseCase @Inject constructor(
    private val photoRepository: PhotoRepository
) {

    operator fun invoke(searchQuery: Flow<String>): Flow<List<Photo>> {
        return combine(
            photoRepository.observePhotos(),
            searchQuery
        ) { photos, rawQuery ->
            val query = rawQuery.normalizeSearchQuery()
            if (query.isBlank()) {
                photos
            } else {
                photos.filter { photo -> photo.matchesShotDate(query) }
            }
        }
    }

    private fun Photo.matchesShotDate(query: String): Boolean {
        return date.toSearchableDateValues().any { value -> value.contains(query) }
    }

    private fun LocalDateTime.toSearchableDateValues(): List<String> {
        return supportedFormats.map { formatter ->
            format(formatter).normalizeSearchQuery()
        }
    }

    private fun String.normalizeSearchQuery(): String {
        return trim().lowercase(searchLocale)
    }

    private companion object {
        private val searchLocale: Locale = Locale.getDefault()
        private val supportedFormats = listOf(
            DateTimeFormatter.ofPattern("dd.MM.yyyy", searchLocale),
            DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm", searchLocale),
            DateTimeFormatter.ofPattern("dd.MM", searchLocale),
            DateTimeFormatter.ofPattern("HH:mm", searchLocale),
            DateTimeFormatter.ofPattern("yyyy-MM", searchLocale),
            DateTimeFormatter.ofPattern("yyyy-MM-dd", searchLocale),
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm", searchLocale),
            DateTimeFormatter.ofPattern("yyyyMMdd", searchLocale)
        )
    }
}
