package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.LocalProxyServer
import com.example.model.AgentProfile
import com.example.model.AuditCamera
import com.example.model.BlockCategory
import com.example.model.CameraZone
import com.example.model.EventSeverity
import com.example.model.PrivacyMask
import com.example.model.PrivacyMaskStyle
import com.example.model.QuizQuestion
import com.example.model.SecurityEvent
import com.example.model.TriageAlert
import com.example.model.TriageDecision
import com.example.model.VisualBlock
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

data class CyberGuardUiState(
    val agentProfile: AgentProfile = AgentProfile(),
    val isGuardingActive: Boolean = true,
    val isNightVision: Boolean = false,
    val isBreachSimulated: Boolean = false,
    val selectedZoneIndex: Int = 0,
    val vpnActive: Boolean = true,
    val firewallActive: Boolean = true,
    val protectionActive: Boolean = true,
    val isProxyServerActive: Boolean = true,
    val proxyPort: Int = 8080,
    val proxyHost: String = "127.0.0.1",
    val proxyRequestsInspected: Long = 1420L,
    val proxyThreatsBlocked: Int = 18,
    val proxyStatusMessage: String = "Proxy Running on 127.0.0.1:8080",
    val proxyTestResult: String? = null,
    val isGeoAutoConnectEnabled: Boolean = true,
    val userLatitude: Double = 37.77490,
    val userLongitude: Double = -122.41940,
    val locationPermissionGranted: Boolean = false,
    val isDeviceCameraActive: Boolean = false,
    val isCameraPermissionGranted: Boolean = false,
    val cameraFacingFront: Boolean = false,
    val isClarityFilterActive: Boolean = false,
    val refocusPulseCount: Int = 0,
    val isBlurDiagnosticOpen: Boolean = false,
    val isPrivacyMaskingEnabled: Boolean = true,
    val privacyMaskStyle: PrivacyMaskStyle = PrivacyMaskStyle.BLACKOUT,
    val selectedPrivacyMaskForDetail: PrivacyMask? = null,
    val privacyMasks: List<PrivacyMask> = listOf(
        PrivacyMask(
            id = "mask_keypad",
            label = "PIN Keypad",
            leftRatio = 0.58f,
            topRatio = 0.44f,
            widthRatio = 0.36f,
            heightRatio = 0.44f,
            isEnabled = true,
            description = "Obscures physical credential entry terminal and PIN keypad"
        ),
        PrivacyMask(
            id = "mask_screen",
            label = "Workstation Display",
            leftRatio = 0.05f,
            topRatio = 0.16f,
            widthRatio = 0.34f,
            heightRatio = 0.36f,
            isEnabled = true,
            description = "Blacks out confidential computer monitor and private screens"
        ),
        PrivacyMask(
            id = "mask_window",
            label = "Perimeter Window",
            leftRatio = 0.64f,
            topRatio = 0.22f,
            widthRatio = 0.30f,
            heightRatio = 0.28f,
            isEnabled = false,
            description = "Complies with privacy regulations by masking adjoining property windows"
        )
    ),
    val geoStatusMessage: String = "GPS Geofence: Auto-connected to Zone 1: Front Entrance (22m)",
    val areAlertsMuted: Boolean = false,
    val activeSeverityFilter: EventSeverity? = null,
    val selectedAlertForDetail: SecurityEvent? = null,
    val disabledEventTypes: Set<String> = emptySet(),
    val cameraZones: List<CameraZone> = listOf(
        CameraZone(
            id = "cam_1",
            name = "Zone 1: Front Entrance",
            location = "Main Outer Perimeter",
            latitude = 37.77492,
            longitude = -122.41945,
            geofenceRadiusMeters = 50f,
            distanceMeters = 22f,
            isWithinGeofence = true,
            autoConnectEnabled = true
        ),
        CameraZone(
            id = "cam_2",
            name = "Zone 2: Data Server Vault",
            location = "Internal Network Core",
            latitude = 37.77520,
            longitude = -122.41915,
            geofenceRadiusMeters = 40f,
            distanceMeters = 65f,
            isWithinGeofence = false,
            autoConnectEnabled = true
        ),
        CameraZone(
            id = "cam_3",
            name = "Zone 3: Logistics Dock",
            location = "Cargo & Loading Bay",
            latitude = 37.77450,
            longitude = -122.41970,
            geofenceRadiusMeters = 70f,
            distanceMeters = 88f,
            isWithinGeofence = false,
            autoConnectEnabled = true
        )
    ),
    val events: List<SecurityEvent> = listOf(
        SecurityEvent(
            id = "err_1",
            timestamp = "Just Now",
            cameraName = "Zone 1: Front Entrance",
            eventType = "CRITICAL: RTSP Stream Cipher Tamper",
            severity = EventSeverity.CRITICAL,
            description = "Cryptographic downgrade attack detected! Cleartext RTSP fallback on port 554 rejected. Stream locked to TLS 1.3.",
            isAuthorized = false,
            errorCode = "ERR_STREAM_CIPHER_0x502",
            remediationAction = "Quarantine rogue IP 185.220.101.44 and enforce SRTP."
        ),
        SecurityEvent(
            id = "err_2",
            timestamp = "4 mins ago",
            cameraName = "Zone 1: Front Entrance",
            eventType = "CRITICAL: Unauthorized Perimeter Motion",
            severity = EventSeverity.CRITICAL,
            description = "Rapid pixel variance detected in restricted safe coordinate box. Automated deterrence strobe activated.",
            isAuthorized = false,
            errorCode = "ERR_INTRUSION_DETECTED_0x01",
            remediationAction = "Review live feed and dispatch physical guard inspection."
        ),
        SecurityEvent(
            id = "err_3",
            timestamp = "12 mins ago",
            cameraName = "Zone 2: Data Vault",
            eventType = "ERROR: Multiple Admin Auth Failures",
            severity = EventSeverity.WARNING,
            description = "Over 6 failed credential attempts in 30 seconds against camera web console. Temporary lockout enforced.",
            isAuthorized = false,
            errorCode = "ERR_AUTH_BRUTE_FORCE_401",
            remediationAction = "Rotate master camera passphrase and enforce 2FA."
        ),
        SecurityEvent(
            id = "err_4",
            timestamp = "35 mins ago",
            cameraName = "Zone 3: Logistics Dock",
            eventType = "WARNING: Camera Ping / Stream Timeout",
            severity = EventSeverity.WARNING,
            description = "RTSP frame stream packet loss exceeded 45%. Potential Wi-Fi attenuation or PoE switch fault.",
            isAuthorized = true,
            errorCode = "ERR_CAM_TIMEOUT_504",
            remediationAction = "Check PoE link integrity and local Wi-Fi channel congestion."
        ),
        SecurityEvent(
            id = "info_1",
            timestamp = "Today, 8:15 AM",
            cameraName = "System Core",
            eventType = "INFO: Systems Online & Guard Armed",
            severity = EventSeverity.INFO,
            description = "Real-time perimeter security daemon initialized. All camera cryptographic certificates valid.",
            isAuthorized = true,
            errorCode = "SYS_DEFENSE_ONLINE_200",
            remediationAction = "System operating within normal security parameters."
        )
    ),
    val triageAlerts: List<TriageAlert> = listOf(
        TriageAlert(
            id = "tri_1",
            title = "Rapid RTSP Port 554 Brute-Force",
            origin = "185.220.101.44 (External Scanner)",
            timestamp = "2 mins ago",
            severity = EventSeverity.CRITICAL,
            description = "Over 120 failed admin credential attempts in 30 seconds against Front Porch Camera gateway.",
            shouldEscalate = true,
            whyEscalateOrDismiss = "Correct! Brute-force attacks must be escalated immediately to trigger perimeter IP blocking."
        ),
        TriageAlert(
            id = "tri_2",
            title = "Authorized NVR Heartbeat Ping",
            origin = "10.0.30.2 (Local Surveillance NVR)",
            timestamp = "14 mins ago",
            severity = EventSeverity.INFO,
            description = "Routine authenticated TLS handshake from authorized local Network Video Recorder.",
            shouldEscalate = false,
            whyEscalateOrDismiss = "Correct! Routine local heartbeats from verified NVRs are expected traffic and can be dismissed safely."
        ),
        TriageAlert(
            id = "tri_3",
            title = "Unencrypted Cleartext RTSP Query",
            origin = "192.168.1.140 (Guest Wi-Fi Device)",
            timestamp = "32 mins ago",
            severity = EventSeverity.WARNING,
            description = "A personal smartphone on Guest Wi-Fi requested unencrypted RTSP video feed on port 554.",
            shouldEscalate = true,
            whyEscalateOrDismiss = "Correct! Unencrypted stream requests from non-isolated subnets should be escalated for VLAN quarantine."
        )
    ),
    val auditCameras: List<AuditCamera> = listOf(
        AuditCamera(
            id = "aud_1",
            name = "Front Porch IP Cam",
            ipAddress = "192.168.1.105",
            port = 554,
            hasDefaultPassword = true,
            isUPnPExposed = true,
            isFirmwareOutdated = false,
            isEncryptionMissing = true,
            isVlanMissing = true
        ),
        AuditCamera(
            id = "aud_2",
            name = "Warehouse South Dome",
            ipAddress = "192.168.1.189",
            port = 8000,
            hasDefaultPassword = false,
            isUPnPExposed = false,
            isFirmwareOutdated = true,
            isEncryptionMissing = true,
            isVlanMissing = true,
            cveIdentifier = "CVE-2021-36260 (Command Injection in Web Server)"
        ),
        AuditCamera(
            id = "aud_3",
            name = "Executive Server Room",
            ipAddress = "10.0.30.22",
            port = 8443,
            hasDefaultPassword = false,
            isUPnPExposed = false,
            isFirmwareOutdated = false,
            isEncryptionMissing = false,
            isVlanMissing = false
        )
    ),
    val selectedBlocks: Set<String> = setOf("ev_motion", "act_alert"),
    val pipelineSimulationResult: String? = null,
    val isPipelineRunning: Boolean = false,
    val quizScore: Int = 0,
    val answeredQuestions: Map<Int, Int> = emptyMap(),
    val showBannerInfo: Boolean = true
) {
    fun filteredEvents(): List<SecurityEvent> {
        val nonDismissed = events.filter { !it.isDismissed && !disabledEventTypes.contains(it.eventType) }
        return if (activeSeverityFilter != null) {
            nonDismissed.filter { it.severity == activeSeverityFilter }
        } else {
            nonDismissed
        }
    }

    fun overallScore(): Int {
        val auditAvg = if (auditCameras.isNotEmpty()) auditCameras.map { it.calculateScore() }.average().toInt() else 100
        val baseScore = (auditAvg * 0.7f + (if (isGuardingActive) 15f else 0f) + (if (vpnActive) 8f else 0f) + (if (firewallActive) 7f else 0f)).toInt()
        return baseScore.coerceIn(20, 100)
    }
}

class CyberGuardViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(CyberGuardUiState())
    val uiState: StateFlow<CyberGuardUiState> = _uiState.asStateFlow()

    private val localProxyServer = LocalProxyServer(defaultPort = 8080)

    init {
        localProxyServer.start()
        viewModelScope.launch {
            localProxyServer.stats.collect { stats ->
                _uiState.update { current ->
                    current.copy(
                        isProxyServerActive = stats.isRunning,
                        proxyPort = stats.port,
                        proxyHost = stats.host,
                        proxyRequestsInspected = stats.totalRequestsInspected,
                        proxyThreatsBlocked = stats.threatsBlocked,
                        proxyStatusMessage = stats.statusMessage
                    )
                }
            }
        }
    }

    fun setLocationPermission(granted: Boolean) {
        _uiState.update { it.copy(locationPermissionGranted = granted) }
    }

    fun setCameraPermission(granted: Boolean) {
        _uiState.update { current ->
            current.copy(
                isCameraPermissionGranted = granted,
                isDeviceCameraActive = if (granted) current.isDeviceCameraActive else false
            )
        }
    }

    fun toggleDeviceCamera(enable: Boolean? = null) {
        _uiState.update { current ->
            val target = enable ?: !current.isDeviceCameraActive
            current.copy(isDeviceCameraActive = target)
        }
    }

    fun toggleCameraFacing() {
        _uiState.update { current ->
            current.copy(cameraFacingFront = !current.cameraFacingFront)
        }
    }

    fun toggleClarityFilter() {
        _uiState.update { current ->
            current.copy(isClarityFilterActive = !current.isClarityFilterActive)
        }
    }

    fun triggerRefocusPulse() {
        _uiState.update { current ->
            current.copy(refocusPulseCount = current.refocusPulseCount + 1)
        }
    }

    fun toggleBlurDiagnostic(open: Boolean? = null) {
        _uiState.update { current ->
            val target = open ?: !current.isBlurDiagnosticOpen
            current.copy(isBlurDiagnosticOpen = target)
        }
    }

    // Privacy Masking Controls
    fun togglePrivacyMasking() {
        _uiState.update { current ->
            current.copy(isPrivacyMaskingEnabled = !current.isPrivacyMaskingEnabled)
        }
    }

    fun togglePrivacyMask(maskId: String) {
        _uiState.update { current ->
            val updated = current.privacyMasks.map { mask ->
                if (mask.id == maskId) mask.copy(isEnabled = !mask.isEnabled) else mask
            }
            current.copy(privacyMasks = updated)
        }
    }

    fun setPrivacyMaskStyle(style: PrivacyMaskStyle) {
        _uiState.update { it.copy(privacyMaskStyle = style) }
    }

    fun selectPrivacyMaskForDetail(mask: PrivacyMask?) {
        _uiState.update { it.copy(selectedPrivacyMaskForDetail = mask) }
    }

    fun addCustomPrivacyMask(
        label: String,
        leftRatio: Float = 0.25f,
        topRatio: Float = 0.25f,
        widthRatio: Float = 0.5f,
        heightRatio: Float = 0.5f
    ) {
        _uiState.update { current ->
            val newMask = PrivacyMask(
                id = "mask_${System.currentTimeMillis()}",
                label = label,
                leftRatio = leftRatio.coerceIn(0.02f, 0.9f),
                topRatio = topRatio.coerceIn(0.02f, 0.9f),
                widthRatio = widthRatio.coerceIn(0.1f, 0.9f),
                heightRatio = heightRatio.coerceIn(0.1f, 0.9f),
                isEnabled = true,
                description = "User-defined sensitive area privacy mask"
            )
            current.copy(privacyMasks = current.privacyMasks + newMask)
        }
    }

    fun removePrivacyMask(maskId: String) {
        _uiState.update { current ->
            current.copy(
                privacyMasks = current.privacyMasks.filterNot { it.id == maskId },
                selectedPrivacyMaskForDetail = if (current.selectedPrivacyMaskForDetail?.id == maskId) null else current.selectedPrivacyMaskForDetail
            )
        }
    }

    fun resetDefaultPrivacyMasks() {
        _uiState.update { current ->
            current.copy(
                isPrivacyMaskingEnabled = true,
                privacyMaskStyle = PrivacyMaskStyle.BLACKOUT,
                privacyMasks = listOf(
                    PrivacyMask(
                        id = "mask_keypad",
                        label = "PIN Keypad",
                        leftRatio = 0.58f,
                        topRatio = 0.44f,
                        widthRatio = 0.36f,
                        heightRatio = 0.44f,
                        isEnabled = true,
                        description = "Obscures physical credential entry terminal and PIN keypad"
                    ),
                    PrivacyMask(
                        id = "mask_screen",
                        label = "Workstation Display",
                        leftRatio = 0.05f,
                        topRatio = 0.16f,
                        widthRatio = 0.34f,
                        heightRatio = 0.36f,
                        isEnabled = true,
                        description = "Blacks out confidential computer monitor and private screens"
                    ),
                    PrivacyMask(
                        id = "mask_window",
                        label = "Perimeter Window",
                        leftRatio = 0.64f,
                        topRatio = 0.22f,
                        widthRatio = 0.30f,
                        heightRatio = 0.28f,
                        isEnabled = false,
                        description = "Complies with privacy regulations by masking adjoining property windows"
                    )
                )
            )
        }
    }

    fun toggleGeoAutoConnect() {
        _uiState.update { current ->
            val newState = !current.isGeoAutoConnectEnabled
            current.copy(
                isGeoAutoConnectEnabled = newState,
                geoStatusMessage = if (newState) "GPS Auto-Connect ARMED: Monitoring nearest camera proximity" else "GPS Auto-Connect PAUSED: Manual camera selection active"
            )
        }
    }

    // Alert Controls & Filtering
    fun toggleMuteAlerts() {
        _uiState.update { it.copy(areAlertsMuted = !it.areAlertsMuted) }
    }

    fun setSeverityFilter(severity: EventSeverity?) {
        _uiState.update { it.copy(activeSeverityFilter = severity) }
    }

    fun selectAlertDetail(event: SecurityEvent?) {
        _uiState.update { it.copy(selectedAlertForDetail = event) }
    }

    fun dismissAlert(alertId: String) {
        _uiState.update { state ->
            state.copy(
                events = state.events.filter { it.id != alertId },
                selectedAlertForDetail = null
            )
        }
    }

    fun clearAllAlerts() {
        _uiState.update { state ->
            state.copy(
                events = emptyList(),
                triageAlerts = emptyList(),
                selectedAlertForDetail = null,
                isBreachSimulated = false,
                agentProfile = state.agentProfile.copy(pendingAlerts = 0)
            )
        }
    }

    fun toggleDisableAlertRule(eventType: String) {
        _uiState.update { state ->
            val updated = state.disabledEventTypes.toMutableSet()
            if (updated.contains(eventType)) {
                updated.remove(eventType)
            } else {
                updated.add(eventType)
            }
            state.copy(disabledEventTypes = updated, selectedAlertForDetail = null)
        }
    }

    // Generate security error alert
    fun triggerSecurityError(preset: String) {
        if (_uiState.value.areAlertsMuted) return

        val newError = when (preset) {
            "BLUR_TAMPER" -> SecurityEvent(
                id = "err_${System.currentTimeMillis()}",
                timestamp = "Just Now",
                cameraName = if (_uiState.value.isDeviceCameraActive) "Device Cam (${if (_uiState.value.cameraFacingFront) "Front" else "Rear"})" else _uiState.value.cameraZones[_uiState.value.selectedZoneIndex].name,
                eventType = "CRITICAL: CCTV Optical Blur / Lens Defocus",
                severity = EventSeverity.CRITICAL,
                description = "Optical MTF score dropped below threshold (severe blur / defocus). Potential lens tampering, dome condensation, or focus drift detected.",
                isAuthorized = false,
                errorCode = "ERR_LENS_DEFOCUS_0x301",
                remediationAction = "Trigger optical auto-refocus sweep, activate Clarity Filter, or wipe moisture from camera dome."
            )
            "CIPHER_ERROR" -> SecurityEvent(
                id = "err_${System.currentTimeMillis()}",
                timestamp = "Just Now",
                cameraName = _uiState.value.cameraZones[_uiState.value.selectedZoneIndex].name,
                eventType = "CRITICAL: RTSP Stream Cipher Tamper",
                severity = EventSeverity.CRITICAL,
                description = "Man-in-the-Middle attack detected on port 554! Rogue gateway attempted certificate downgrade.",
                isAuthorized = false,
                errorCode = "ERR_STREAM_CIPHER_0x502",
                remediationAction = "Enforce SRTP / TLS 1.3 encryption and quarantine source IP."
            )
            "AUTH_ERROR" -> SecurityEvent(
                id = "err_${System.currentTimeMillis()}",
                timestamp = "Just Now",
                cameraName = "Zone 2: Data Server Vault",
                eventType = "ERROR: Brute-Force Password Lockout",
                severity = EventSeverity.CRITICAL,
                description = "Automated dictionary attack on camera admin credentials. Host temporarily banned for 15 minutes.",
                isAuthorized = false,
                errorCode = "ERR_AUTH_BRUTE_FORCE_401",
                remediationAction = "Rotate password to 18-char passphrase and enable multi-factor auth."
            )
            "CAM_OFFLINE" -> SecurityEvent(
                id = "err_${System.currentTimeMillis()}",
                timestamp = "Just Now",
                cameraName = "Zone 3: Logistics Dock",
                eventType = "WARNING: Camera Signal Lost",
                severity = EventSeverity.WARNING,
                description = "RTSP frame stream dropped below threshold (0 fps). Cable disconnect or power interruption.",
                isAuthorized = true,
                errorCode = "ERR_CAM_TIMEOUT_504",
                remediationAction = "Check camera power supply and network switch port status."
            )
            else -> SecurityEvent(
                id = "err_${System.currentTimeMillis()}",
                timestamp = "Just Now",
                cameraName = _uiState.value.cameraZones[_uiState.value.selectedZoneIndex].name,
                eventType = "CRITICAL: Unauthorized Perimeter Motion Alert",
                severity = EventSeverity.CRITICAL,
                description = "Unidentified body signature crossed virtual boundary line. Siren triggered.",
                isAuthorized = false,
                errorCode = "ERR_INTRUSION_DETECTED_0x01",
                remediationAction = "Verify security guard log and check live feed."
            )
        }

        _uiState.update { current ->
            current.copy(
                events = listOf(newError) + current.events.take(15),
                isBreachSimulated = if (newError.severity == EventSeverity.CRITICAL) true else current.isBreachSimulated
            )
        }
    }

    private fun calculateDistanceMeters(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Float {
        val earthRadius = 6371000.0 // meters
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2) * sin(dLon / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return (earthRadius * c).toFloat()
    }

    fun updateUserLocation(userLat: Double, userLon: Double) {
        _uiState.update { state ->
            val updatedZones = state.cameraZones.map { cam ->
                val dist = calculateDistanceMeters(userLat, userLon, cam.latitude, cam.longitude)
                val withinGeofence = dist <= cam.geofenceRadiusMeters
                cam.copy(
                    distanceMeters = dist,
                    isWithinGeofence = withinGeofence
                )
            }

            val closestIndex = updatedZones.indices.minByOrNull { updatedZones[it].distanceMeters } ?: 0
            val closestCam = updatedZones[closestIndex]

            val autoSwitched = state.isGeoAutoConnectEnabled && closestIndex != state.selectedZoneIndex
            val newSelectedIdx = if (state.isGeoAutoConnectEnabled) closestIndex else state.selectedZoneIndex

            val newEvent = if (autoSwitched && !state.areAlertsMuted && !state.disabledEventTypes.contains("Geo-Fence Auto-Connected")) {
                SecurityEvent(
                    id = "ev_geo_${System.currentTimeMillis()}",
                    timestamp = "Just Now",
                    cameraName = closestCam.name,
                    eventType = "Geo-Fence Auto-Connected",
                    severity = EventSeverity.INFO,
                    description = "User entered geofence boundary (${closestCam.distanceMeters.toInt()}m from camera). Stream automatically routed to encrypted preview.",
                    isAuthorized = true,
                    errorCode = "GEO_LOCK_200",
                    remediationAction = "Encrypted RTSP preview active."
                )
            } else null

            val newEventsList = if (newEvent != null) listOf(newEvent) + state.events.take(15) else state.events

            val statusMsg = if (closestCam.isWithinGeofence) {
                "Proximity Lock: Auto-connected to ${closestCam.name} (${closestCam.distanceMeters.toInt()}m away)"
            } else {
                "Perimeter Geofence: Outside safe boundary (${closestCam.distanceMeters.toInt()}m to nearest camera)"
            }

            state.copy(
                userLatitude = userLat,
                userLongitude = userLon,
                cameraZones = updatedZones,
                selectedZoneIndex = newSelectedIdx,
                geoStatusMessage = statusMsg,
                events = newEventsList
            )
        }
    }

    fun simulateLocationPreset(presetKey: String) {
        when (presetKey) {
            "ZONE_1" -> updateUserLocation(37.77492, -122.41945)
            "ZONE_2" -> updateUserLocation(37.77520, -122.41915)
            "ZONE_3" -> updateUserLocation(37.77450, -122.41970)
            "OUTSIDE" -> updateUserLocation(37.78150, -122.41200)
        }
    }

    fun toggleGuarding() {
        _uiState.update { current ->
            val newState = !current.isGuardingActive
            val eventType = if (newState) "Perimeter Guard Armed" else "System Disarmed"
            val shouldLog = !current.areAlertsMuted && !current.disabledEventTypes.contains(eventType)

            val newEvent = if (shouldLog) {
                SecurityEvent(
                    id = "ev_${System.currentTimeMillis()}",
                    timestamp = "Just Now",
                    cameraName = current.cameraZones[current.selectedZoneIndex].name,
                    eventType = eventType,
                    severity = if (newState) EventSeverity.INFO else EventSeverity.WARNING,
                    description = if (newState) "Defensive surveillance sensors online. Motion boundary active." else "Perimeter protection paused by authorized administrator.",
                    isAuthorized = true,
                    errorCode = if (newState) "GUARD_ARMED_200" else "GUARD_STANDBY_100",
                    remediationAction = if (newState) "Surveillance perimeter monitoring active." else "Tap Start Guarding to re-arm."
                )
            } else null

            val updatedEvents = if (newEvent != null) listOf(newEvent) + current.events.take(15) else current.events

            current.copy(
                isGuardingActive = newState,
                isBreachSimulated = if (!newState) false else current.isBreachSimulated,
                events = updatedEvents
            )
        }
    }

    fun toggleVpn() {
        _uiState.update { it.copy(vpnActive = !it.vpnActive) }
    }

    fun toggleFirewall() {
        _uiState.update { it.copy(firewallActive = !it.firewallActive) }
    }

    fun toggleProtection() {
        _uiState.update { it.copy(protectionActive = !it.protectionActive) }
    }

    fun toggleProxyServer() {
        val willBeActive = !_uiState.value.isProxyServerActive
        if (_uiState.value.isProxyServerActive) {
            localProxyServer.stop()
        } else {
            localProxyServer.start()
        }
        _uiState.update { it.copy(isProxyServerActive = willBeActive) }
    }

    fun setProxyPort(port: Int) {
        localProxyServer.setPort(port)
        _uiState.update { it.copy(proxyPort = port) }
    }

    fun testProxyProbe(isMalicious: Boolean = false) {
        localProxyServer.sendTestProxyProbe(isMalicious) { resultMessage ->
            _uiState.update { it.copy(proxyTestResult = resultMessage) }
        }
    }

    fun dismissProxyTestResult() {
        _uiState.update { it.copy(proxyTestResult = null) }
    }

    fun toggleNightVision() {
        _uiState.update { it.copy(isNightVision = !it.isNightVision) }
    }

    fun selectZone(index: Int) {
        _uiState.update { it.copy(selectedZoneIndex = index, isBreachSimulated = false) }
    }

    fun simulateBreach() {
        if (!_uiState.value.isGuardingActive) return

        _uiState.update { current ->
            val breachEvent = SecurityEvent(
                id = "ev_${System.currentTimeMillis()}",
                timestamp = "Just Now",
                cameraName = current.cameraZones[current.selectedZoneIndex].name,
                eventType = "CRITICAL: UNAUTHORIZED PERIMETER MOTION",
                severity = EventSeverity.CRITICAL,
                description = "Rapid pixel delta inside restricted safe zone! Triggering defensive protocol: Floodlight ON, Snapshot Encrypted, Guard Notification dispatched.",
                isAuthorized = false,
                errorCode = "ERR_INTRUSION_DETECTED_0x01",
                remediationAction = "Verify security guard log and check live feed."
            )
            current.copy(
                isBreachSimulated = true,
                events = if (!current.areAlertsMuted) listOf(breachEvent) + current.events.take(15) else current.events
            )
        }
    }

    fun dismissBreach() {
        _uiState.update { it.copy(isBreachSimulated = false) }
    }

    // Triage alert decision
    fun triageAlert(alertId: String, decision: TriageDecision) {
        _uiState.update { state ->
            val updatedAlerts = state.triageAlerts.map { alert ->
                if (alert.id == alertId) {
                    val isCorrect = (decision == TriageDecision.ESCALATED && alert.shouldEscalate) ||
                            (decision == TriageDecision.DISMISSED && !alert.shouldEscalate)
                    alert.copy(userDecision = decision, isResolvedCorrectly = isCorrect)
                } else {
                    alert
                }
            }

            val pendingCount = updatedAlerts.count { it.userDecision == TriageDecision.PENDING }
            val xpGain = 50
            val newTotalXp = state.agentProfile.totalXp + xpGain
            val newLevel = if (newTotalXp >= state.agentProfile.nextLevelXp) state.agentProfile.level + 1 else state.agentProfile.level
            val updatedProfile = state.agentProfile.copy(
                totalXp = newTotalXp,
                level = newLevel,
                pendingAlerts = pendingCount
            )

            state.copy(
                triageAlerts = updatedAlerts,
                agentProfile = updatedProfile
            )
        }
    }

    fun resetTriage() {
        _uiState.update { state ->
            val resetAlerts = state.triageAlerts.map { it.copy(userDecision = TriageDecision.PENDING, isResolvedCorrectly = null) }
            state.copy(
                triageAlerts = resetAlerts,
                agentProfile = state.agentProfile.copy(pendingAlerts = resetAlerts.size)
            )
        }
    }

    fun clearAllTriageAlerts() {
        _uiState.update { state ->
            state.copy(
                triageAlerts = emptyList(),
                agentProfile = state.agentProfile.copy(pendingAlerts = 0)
            )
        }
    }

    fun removeTriageAlert(alertId: String) {
        _uiState.update { state ->
            val updated = state.triageAlerts.filter { it.id != alertId }
            state.copy(
                triageAlerts = updated,
                agentProfile = state.agentProfile.copy(pendingAlerts = updated.count { it.userDecision == TriageDecision.PENDING })
            )
        }
    }

    fun removeAllAlertsAndErrors() {
        _uiState.update { state ->
            state.copy(
                events = emptyList(),
                triageAlerts = emptyList(),
                selectedAlertForDetail = null,
                isBreachSimulated = false,
                agentProfile = state.agentProfile.copy(pendingAlerts = 0)
            )
        }
    }

    fun reloadSampleTriageAlerts() {
        _uiState.update { state ->
            val sample = listOf(
                TriageAlert(
                    id = "tri_1",
                    title = "Rapid RTSP Port 554 Brute-Force",
                    origin = "185.220.101.44 (External Scanner)",
                    timestamp = "2 mins ago",
                    severity = EventSeverity.CRITICAL,
                    description = "Over 120 failed admin credential attempts in 30 seconds against Front Porch Camera gateway.",
                    shouldEscalate = true,
                    whyEscalateOrDismiss = "Correct! Brute-force attacks must be escalated immediately to trigger perimeter IP blocking."
                ),
                TriageAlert(
                    id = "tri_2",
                    title = "Authorized NVR Heartbeat Ping",
                    origin = "10.0.30.2 (Local Surveillance NVR)",
                    timestamp = "14 mins ago",
                    severity = EventSeverity.INFO,
                    description = "Routine authenticated TLS handshake from authorized local Network Video Recorder.",
                    shouldEscalate = false,
                    whyEscalateOrDismiss = "Correct! Routine local heartbeats from verified NVRs are expected traffic and can be dismissed safely."
                ),
                TriageAlert(
                    id = "tri_3",
                    title = "Unencrypted Cleartext RTSP Query",
                    origin = "192.168.1.140 (Guest Wi-Fi Device)",
                    timestamp = "32 mins ago",
                    severity = EventSeverity.WARNING,
                    description = "A personal smartphone on Guest Wi-Fi requested unencrypted RTSP video feed on port 554.",
                    shouldEscalate = true,
                    whyEscalateOrDismiss = "Correct! Unencrypted stream requests from non-isolated subnets should be escalated for VLAN quarantine."
                )
            )
            state.copy(
                triageAlerts = sample,
                agentProfile = state.agentProfile.copy(pendingAlerts = sample.size)
            )
        }
    }

    // Camera Hardening Audit Actions
    fun fixAuditPassword(cameraId: String) {
        _uiState.update { state ->
            state.copy(
                auditCameras = state.auditCameras.map { cam ->
                    if (cam.id == cameraId) cam.copy(hasDefaultPassword = false) else cam
                }
            )
        }
    }

    fun fixAuditUPnP(cameraId: String) {
        _uiState.update { state ->
            state.copy(
                auditCameras = state.auditCameras.map { cam ->
                    if (cam.id == cameraId) cam.copy(isUPnPExposed = false) else cam
                }
            )
        }
    }

    fun fixAuditFirmware(cameraId: String) {
        _uiState.update { state ->
            state.copy(
                auditCameras = state.auditCameras.map { cam ->
                    if (cam.id == cameraId) cam.copy(isFirmwareOutdated = false, cveIdentifier = null) else cam
                }
            )
        }
    }

    fun fixAuditEncryption(cameraId: String) {
        _uiState.update { state ->
            state.copy(
                auditCameras = state.auditCameras.map { cam ->
                    if (cam.id == cameraId) cam.copy(isEncryptionMissing = false) else cam
                }
            )
        }
    }

    fun fixAuditVlan(cameraId: String) {
        _uiState.update { state ->
            state.copy(
                auditCameras = state.auditCameras.map { cam ->
                    if (cam.id == cameraId) cam.copy(isVlanMissing = false) else cam
                }
            )
        }
    }

    // Visual Block Logic Lab
    fun toggleBlock(blockId: String) {
        _uiState.update { state ->
            val set = state.selectedBlocks.toMutableSet()
            if (set.contains(blockId)) set.remove(blockId) else set.add(blockId)
            state.copy(selectedBlocks = set, pipelineSimulationResult = null)
        }
    }

    fun runPipelineSimulation() {
        viewModelScope.launch {
            _uiState.update { it.copy(isPipelineRunning = true, pipelineSimulationResult = null) }
            delay(1200)
            val selected = _uiState.value.selectedBlocks
            val result = when {
                selected.contains("ev_motion") && selected.contains("cond_unknown") && selected.contains("act_alert") ->
                    "SUCCESS: When Motion is detected from an Unknown Entity -> System safely activates defensive siren and logs encrypted timestamp!"
                selected.contains("ev_motion") && selected.contains("act_floodlight") ->
                    "ACTIVE: Motion triggered automated perimeter floodlights. Night vision maintained."
                selected.contains("ev_start_btn") && selected.contains("act_arm") ->
                    "ARMED: Start_Btn tapped -> CyberGuard enters Active Defense Mode!"
                else ->
                    "SIMULATED: Custom pipeline executed. Events evaluated defensively against security rules."
            }
            _uiState.update { it.copy(isPipelineRunning = false, pipelineSimulationResult = result) }
        }
    }

    // Quiz logic
    fun answerQuestion(questionId: Int, selectedIndex: Int, isCorrect: Boolean) {
        _uiState.update { state ->
            val newAnswers = state.answeredQuestions + (questionId to selectedIndex)
            val newScore = if (isCorrect && !state.answeredQuestions.containsKey(questionId)) state.quizScore + 1 else state.quizScore
            val xpGain = if (isCorrect && !state.answeredQuestions.containsKey(questionId)) 20 else 0
            val updatedProfile = state.agentProfile.copy(totalXp = state.agentProfile.totalXp + xpGain)
            state.copy(answeredQuestions = newAnswers, quizScore = newScore, agentProfile = updatedProfile)
        }
    }

    fun dismissBanner() {
        _uiState.update { it.copy(showBannerInfo = false) }
    }

    override fun onCleared() {
        super.onCleared()
        localProxyServer.stop()
    }
}
