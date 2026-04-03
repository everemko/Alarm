package com.example.alarm.ui.photos

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.alarm.R
import com.example.alarm.domain.entity.Photo
import com.example.alarm.ui.common.ScreenToolbar
import com.example.alarm.ui.theme.AlarmTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.time.ZoneId
import androidx.core.net.toUri
import java.time.LocalDateTime

@Composable
fun PhotosScreen(
    photos: List<Photo>,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onBack: () -> Unit,
    onPhotoClick: (String) -> Unit
) {
    val unknownDateLabel = stringResource(R.string.photos_date_unknown)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        ScreenToolbar(
            title = stringResource(R.string.screen_photos_title),
            onBack = onBack
        )

        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            label = { Text(stringResource(R.string.photos_search_date_label)) },
            placeholder = { Text(stringResource(R.string.photos_search_date_placeholder)) }
        )

        if (photos.isEmpty()) {
            Text(
                stringResource(
                    if (searchQuery.isBlank()) {
                        R.string.photos_empty
                    } else {
                        R.string.photos_search_empty
                    }
                )
            )
            return@Column
        }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(items = photos, key = { it.id }) { photo ->
                ElevatedCard(
                    onClick = { onPhotoClick(photo.id) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(photo.name)
                        val timestamp = photo.date
                            .atZone(ZoneId.systemDefault())
                            .toInstant()
                            .toEpochMilli()
                        Text(
                            stringResource(
                                R.string.photos_modified,
                                formatDate(timestamp, unknownDateLabel)
                            )
                        )
                        Text(photo.uri.toString())
                    }
                }
            }
        }
    }
}

private fun formatDate(timestamp: Long, unknownDateLabel: String): String {
    if (timestamp <= 0L) return unknownDateLabel
    return SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault()).format(Date(timestamp))
}

@Preview(showBackground = true)
@Composable
private fun PhotosScreenPreview() {
    AlarmTheme {
        PhotosScreen(
            photos = listOf(
                Photo(
                    id = "photo_preview_1",
                    name = "ALARM_20260331_120000.jpg",
                    uri = "content://photos/1".toUri(),
                    date = LocalDateTime.of(2026, 3, 31, 12, 0),
                    coordinates = null
                ),
                Photo(
                    id = "photo_preview_2",
                    name = "ALARM_20260331_120500.jpg",
                    uri = "content://photos/2".toUri(),
                    date = LocalDateTime.of(2026, 3, 31, 12, 5),
                    coordinates = null
                )
            ),
            searchQuery = "",
            onSearchQueryChange = {},
            onBack = {},
            onPhotoClick = {}
        )
    }
}
