package com.example.alarm.ui.photo

import android.net.Uri
import android.widget.ImageView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.alarm.R
import com.example.alarm.ui.common.ScreenToolbar
import java.text.SimpleDateFormat
import java.time.ZoneId
import java.util.Date
import java.util.Locale

@Composable
fun PhotoDetailsScreen(
    uiState: PhotoDetailsUiState,
    onBack: () -> Unit
) {
    val unknownDateLabel = stringResource(R.string.photos_date_unknown)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        ScreenToolbar(
            title = stringResource(R.string.screen_photo_details_title),
            onBack = onBack
        )

        when {
            uiState.isLoading -> Text(stringResource(R.string.photo_details_loading))
            uiState.photo == null -> Text(stringResource(R.string.photo_details_not_found))
            else -> {
                val photo = requireNotNull(uiState.photo)
                val timestamp = photo.date
                    .atZone(ZoneId.systemDefault())
                    .toInstant()
                    .toEpochMilli()

                PhotoContent(
                    uri = photo.uri,
                    name = photo.name,
                    modifiedLabel = stringResource(
                        R.string.photos_modified,
                        formatDate(timestamp, unknownDateLabel)
                    ),
                    uriLabel = stringResource(R.string.photo_details_uri, photo.uri.toString())
                )
            }
        }
    }
}

@Composable
private fun PhotoContent(
    uri: Uri,
    name: String,
    modifiedLabel: String,
    uriLabel: String
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        AndroidView(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(3f / 4f)
                .background(Color.Black),
            factory = { context ->
                ImageView(context).apply {
                    adjustViewBounds = true
                    scaleType = ImageView.ScaleType.FIT_CENTER
                    setBackgroundColor(android.graphics.Color.BLACK)
                }
            },
            update = { imageView ->
                imageView.setImageURI(uri)
            }
        )

        Text(name)
        Text(modifiedLabel)
        Text(uriLabel)
    }
}

private fun formatDate(timestamp: Long, unknownDateLabel: String): String {
    if (timestamp <= 0L) return unknownDateLabel
    return SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault()).format(Date(timestamp))
}
