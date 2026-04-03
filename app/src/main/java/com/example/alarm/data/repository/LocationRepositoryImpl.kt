package com.example.alarm.data.repository

import android.content.Context
import android.location.Location
import android.location.LocationManager
import androidx.core.content.ContextCompat
import com.example.alarm.data.system.LocationProviderObserver
import com.example.alarm.domain.entity.Coordinates
import com.example.alarm.domain.repository.LocationRepository
import com.example.alarm.domain.repository.PermissionRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.runningFold
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationRepositoryImpl @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val permissionRepository: PermissionRepository,
    private val locationProviderObserver: LocationProviderObserver
) : LocationRepository {

    private val locationManager: LocationManager? by lazy {
        ContextCompat.getSystemService(context, LocationManager::class.java)
    }

    override fun observeLocation(): Flow<Coordinates> {
        val manager = locationManager ?: return emptyFlow()
        val providers = activeProviders(manager)
        if (providers.isEmpty()) return emptyFlow()

        return providers.map { provider ->
            locationProviderObserver.observe(
                manager = manager,
                provider = provider,
                updateIntervalMillis = LOCATION_UPDATE_INTERVAL_MILLIS,
            )
        }
            .merge()
            .runningFold<Location, Location?>(null) { currentBest, candidate ->
                selectBetterLocation(
                    currentBest = currentBest,
                    candidate = candidate
                )
            }
            .filterNotNull()
            .map { result -> result.toCoordinates() }
            .distinctUntilChanged()
    }

    private fun activeProviders(
        manager: LocationManager,
    ): List<String> {
        return buildList {
            if (permissionRepository.hasFineLocationPermission()) {
                add(LocationManager.GPS_PROVIDER)
            }
            add(LocationManager.NETWORK_PROVIDER)
            add(LocationManager.PASSIVE_PROVIDER)
        }.distinct()
            .filter { provider ->
                hasPermissionForProvider(provider) &&
                        runCatching { manager.isProviderEnabled(provider) }
                            .getOrDefault(false)
            }
    }

    private fun hasPermissionForProvider(provider: String): Boolean {
        return when (provider) {
            LocationManager.GPS_PROVIDER -> permissionRepository.hasFineLocationPermission()
            else -> permissionRepository.hasLocationPermission()
        }
    }

    private fun selectBetterLocation(
        currentBest: Location?,
        candidate: Location
    ): Location {
        val best = currentBest ?: return candidate

        val bestProviderRank = providerRank(best.provider)
        val candidateProviderRank = providerRank(candidate.provider)

        if (candidateProviderRank < bestProviderRank) return candidate
        if (candidateProviderRank > bestProviderRank) return best

        val bestAccuracy = best.accuracyOrNull()
        val candidateAccuracy = candidate.accuracyOrNull()

        return when {
            bestAccuracy == null && candidateAccuracy != null -> candidate
            bestAccuracy != null && candidateAccuracy == null -> best
            bestAccuracy != null && candidateAccuracy != null && candidateAccuracy < bestAccuracy -> candidate
            bestAccuracy != null && candidateAccuracy != null && candidateAccuracy > bestAccuracy -> best
            candidate.time > best.time -> candidate
            else -> best
        }
    }

    private fun providerRank(provider: String?): Int {
        return when (provider) {
            LocationManager.GPS_PROVIDER -> 0
            LocationManager.NETWORK_PROVIDER -> 1
            LocationManager.PASSIVE_PROVIDER -> 2
            else -> 3
        }
    }

    private fun Location.accuracyOrNull(): Float? {
        return if (hasAccuracy()) accuracy else null
    }

    private fun Location.toCoordinates(): Coordinates {
        return this.let { Coordinates(latitude = it.latitude, longitude = it.longitude) }
    }

    private companion object {
        const val LOCATION_UPDATE_INTERVAL_MILLIS = 1_000L
    }
}
