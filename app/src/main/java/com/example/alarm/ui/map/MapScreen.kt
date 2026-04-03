package com.example.alarm.ui.map

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.alarm.R
import com.example.alarm.domain.entity.Coordinates
import com.example.alarm.ui.common.ScreenToolbar
import com.example.alarm.ui.theme.AlarmTheme
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import org.maplibre.compose.camera.CameraPosition
import org.maplibre.compose.camera.rememberCameraState
import org.maplibre.compose.expressions.dsl.const
import org.maplibre.compose.layers.CircleLayer
import org.maplibre.compose.map.MaplibreMap
import org.maplibre.compose.sources.GeoJsonData
import org.maplibre.compose.sources.rememberGeoJsonSource
import org.maplibre.compose.style.BaseStyle
import org.maplibre.spatialk.geojson.Feature
import org.maplibre.spatialk.geojson.FeatureCollection
import org.maplibre.spatialk.geojson.Point
import org.maplibre.spatialk.geojson.Position

private const val OPEN_FREE_MAP_STYLE_URL = "https://tiles.openfreemap.org/styles/liberty"
private const val PHOTO_MARKERS_LAYER_ID = "photo-markers"

@Composable
fun MapScreen(
    uiState: MapUiState,
    onBack: () -> Unit,
    mapContent: @Composable (Modifier, MapUiState) -> Unit = { modifier, currentUiState ->
        MapLibrePhotoMap(
            modifier = modifier,
            uiState = currentUiState
        )
    }
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        ScreenToolbar(
            title = stringResource(R.string.screen_map_title),
            onBack = onBack
        )

        mapContent(
            Modifier
                .fillMaxWidth()
                .weight(1f),
            uiState
        )
    }
}

@Composable
private fun MapLibrePhotoMap(
    modifier: Modifier = Modifier,
    uiState: MapUiState
) {
    val cameraState = rememberCameraState(
        firstPosition = uiState.camera.toCameraPosition()
    )
    val photoMarkersData = remember(uiState.coordinates) {
        uiState.coordinates
            .takeIf { coordinates -> coordinates.isNotEmpty() }
            ?.let { coordinates ->
                GeoJsonData.Features(coordinates.toFeatureCollection())
            }
    }

    LaunchedEffect(uiState.camera) {
        cameraState.position = uiState.camera.toCameraPosition()
    }

    MaplibreMap(
        modifier = modifier,
        baseStyle = BaseStyle.Uri(OPEN_FREE_MAP_STYLE_URL),
        cameraState = cameraState
    ) {
        if (photoMarkersData != null) {
            val photoMarkersSource = rememberGeoJsonSource(
                data = photoMarkersData
            )

            CircleLayer(
                id = PHOTO_MARKERS_LAYER_ID,
                source = photoMarkersSource,
                color = const(MaterialTheme.colorScheme.primary),
                radius = const(8.dp),
                strokeColor = const(MaterialTheme.colorScheme.onPrimary),
                strokeWidth = const(2.dp)
            )
        }
    }
}

@Composable
private fun MapPlaceholder(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.background(Color(0xFF2E7D32)),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.map_placeholder),
            color = Color.White,
            modifier = Modifier.padding(16.dp)
        )
    }
}

private fun MapCamera.toCameraPosition(): CameraPosition {
    return CameraPosition(
        target = Position(
            longitude = longitude,
            latitude = latitude
        ),
        zoom = zoom
    )
}

private fun List<Coordinates>.toFeatureCollection(): FeatureCollection<Point, JsonObject> {
    return FeatureCollection(
        features = map { coordinates ->
            Feature(
                geometry = Point(
                    longitude = coordinates.longitude,
                    latitude = coordinates.latitude
                ),
                properties = buildJsonObject {}
            )
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun MapScreenPreview() {
    AlarmTheme {
        MapScreen(
            uiState = MapUiState(
                coordinates = listOf(
                    Coordinates(
                        latitude = 53.9045,
                        longitude = 27.5615
                    )
                ),
                camera = MapCamera(
                    latitude = 53.9045,
                    longitude = 27.5615,
                    zoom = 14.0
                )
            ),
            onBack = {},
            mapContent = { modifier, _ ->
                MapPlaceholder(modifier = modifier)
            }
        )
    }
}
