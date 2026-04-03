package com.example.alarm.data.system

import android.annotation.SuppressLint
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Looper
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class LocationProviderObserver @Inject constructor() {

    @SuppressLint("MissingPermission")
    fun observe(
        manager: LocationManager,
        provider: String,
        updateIntervalMillis: Long,
    ): Flow<Location> = callbackFlow {
        manager.getLastKnownLocation(provider)
            ?.let { location -> trySend(location) }

        val listener = LocationListener { location -> trySend(location) }

        runCatching {
            manager.requestLocationUpdates(
                provider,
                updateIntervalMillis,
                10f,
                listener,
                Looper.getMainLooper()
            )
        }.onFailure {
            close(it)
            return@callbackFlow
        }

        awaitClose {
            runCatching { manager.removeUpdates(listener) }
        }
    }
}
