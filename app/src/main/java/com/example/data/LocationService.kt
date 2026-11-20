package com.example.data

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

/**
 * Encapsulates Google Play Services Location (FusedLocationProviderClient)
 * to retrieve real-time device latitude and longitude for automated CCTV
 * camera discovery and proximity geofencing.
 */
class LocationService(
    private val context: Context,
    private val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
) {

    data class Coordinates(
        val latitude: Double,
        val longitude: Double,
        val accuracyMeters: Float = 0f,
        val provider: String = "fused_play_services"
    )

    /**
     * Checks if either fine or coarse location permissions have been granted.
     */
    fun hasLocationPermission(): Boolean {
        val finePermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val coarsePermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        return finePermission || coarsePermission
    }

    /**
     * Retrieves the current device coordinates asynchronously with a callback.
     */
    @SuppressLint("MissingPermission")
    fun getCurrentCoordinates(
        onSuccess: (Coordinates) -> Unit
    ) {
        getCurrentCoordinates(onSuccess = onSuccess, onFailure = {})
    }

    /**
     * Retrieves the current device coordinates asynchronously with success & failure callbacks.
     * Uses Play Services high accuracy with fallback to system LocationManager.
     */
    @SuppressLint("MissingPermission")
    fun getCurrentCoordinates(
        onSuccess: (Coordinates) -> Unit,
        onFailure: (Exception?) -> Unit
    ) {
        if (!hasLocationPermission()) {
            onFailure(SecurityException("Location permissions not granted"))
            return
        }

        try {
            val cancellationTokenSource = CancellationTokenSource()

            fusedLocationClient.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                cancellationTokenSource.token
            ).addOnSuccessListener { location: Location? ->
                if (location != null) {
                    onSuccess(
                        Coordinates(
                            latitude = location.latitude,
                            longitude = location.longitude,
                            accuracyMeters = location.accuracy,
                            provider = "fused_play_services"
                        )
                    )
                } else {
                    // If high accuracy current location returned null, query last known location
                    getLastKnownCoordinates(onSuccess, onFailure)
                }
            }.addOnFailureListener { exception ->
                // Play services failed; attempt system LocationManager fallback
                fallbackToSystemLocationManager(onSuccess, onFailure)
            }
        } catch (e: SecurityException) {
            onFailure(e)
        } catch (e: Exception) {
            fallbackToSystemLocationManager(onSuccess, onFailure)
        }
    }

    /**
     * Suspendable retrieval of the device coordinates for coroutine workflows.
     */
    @SuppressLint("MissingPermission")
    suspend fun getCurrentLocation(): Coordinates? = suspendCancellableCoroutine { continuation ->
        if (!hasLocationPermission()) {
            continuation.resume(null)
            return@suspendCancellableCoroutine
        }

        getCurrentCoordinates(
            onSuccess = { coords ->
                if (continuation.isActive) continuation.resume(coords)
            },
            onFailure = {
                if (continuation.isActive) continuation.resume(null)
            }
        )
    }

    /**
     * Retrieves the last cached location from Play Services with single success callback.
     */
    @SuppressLint("MissingPermission")
    fun getLastKnownCoordinates(
        onSuccess: (Coordinates) -> Unit
    ) {
        getLastKnownCoordinates(onSuccess = onSuccess, onFailure = {})
    }

    /**
     * Retrieves the last cached location from Play Services.
     */
    @SuppressLint("MissingPermission")
    fun getLastKnownCoordinates(
        onSuccess: (Coordinates) -> Unit,
        onFailure: (Exception?) -> Unit
    ) {
        if (!hasLocationPermission()) {
            onFailure(SecurityException("Location permissions not granted"))
            return
        }

        try {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        onSuccess(
                            Coordinates(
                                latitude = location.latitude,
                                longitude = location.longitude,
                                accuracyMeters = location.accuracy,
                                provider = "fused_play_services_last"
                            )
                        )
                    } else {
                        fallbackToSystemLocationManager(onSuccess, onFailure)
                    }
                }
                .addOnFailureListener {
                    fallbackToSystemLocationManager(onSuccess, onFailure)
                }
        } catch (e: Exception) {
            fallbackToSystemLocationManager(onSuccess, onFailure)
        }
    }

    /**
     * Provides a cold Flow of live location updates for active camera proximity discovery.
     */
    @SuppressLint("MissingPermission")
    fun getLocationUpdates(intervalMs: Long = 10000L): Flow<Coordinates> = callbackFlow {
        if (!hasLocationPermission()) {
            close(SecurityException("Location permissions missing for location updates"))
            return@callbackFlow
        }

        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, intervalMs)
            .setMinUpdateIntervalMillis(intervalMs / 2)
            .build()

        val callback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                result.lastLocation?.let { loc ->
                    trySend(
                        Coordinates(
                            latitude = loc.latitude,
                            longitude = loc.longitude,
                            accuracyMeters = loc.accuracy,
                            provider = "fused_stream"
                        )
                    )
                }
            }
        }

        try {
            fusedLocationClient.requestLocationUpdates(locationRequest, callback, context.mainLooper)
        } catch (e: Exception) {
            close(e)
        }

        awaitClose {
            fusedLocationClient.removeLocationUpdates(callback)
        }
    }

    /**
     * Resilient fallback to system LocationManager (GPS/Network) when Play Services
     * is unavailable (e.g. testing environments, AOSP without Google Mobile Services).
     */
    @SuppressLint("MissingPermission")
    private fun fallbackToSystemLocationManager(
        onSuccess: (Coordinates) -> Unit,
        onFailure: (Exception?) -> Unit = {}
    ) {
        try {
            val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager
            if (locationManager == null) {
                onFailure(IllegalStateException("LocationManager service unavailable"))
                return
            }

            val gpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            val networkLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)

            val bestLocation = when {
                gpsLocation != null && networkLocation != null -> {
                    if (gpsLocation.time > networkLocation.time) gpsLocation else networkLocation
                }
                gpsLocation != null -> gpsLocation
                networkLocation != null -> networkLocation
                else -> null
            }

            if (bestLocation != null) {
                onSuccess(
                    Coordinates(
                        latitude = bestLocation.latitude,
                        longitude = bestLocation.longitude,
                        accuracyMeters = bestLocation.accuracy,
                        provider = bestLocation.provider ?: "system_fallback"
                    )
                )
            } else {
                onFailure(NoSuchElementException("No location available from system providers"))
            }
        } catch (e: Exception) {
            onFailure(e)
        }
    }
}
