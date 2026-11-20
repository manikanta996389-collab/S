package com.example.ui.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoFixHigh
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.CenterFocusStrong
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.GpsFixed
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.NotificationsOff
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Router
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.VideocamOff
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import com.example.data.LocationService
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.model.EventSeverity
import com.example.model.PrivacyMask
import com.example.model.PrivacyMaskStyle
import com.example.model.SecurityEvent
import com.example.ui.components.CameraMonitorView
import com.example.ui.components.CircularScoreGauge
import com.example.ui.components.GeoCameraRadarView
import com.example.ui.theme.CyberAmber
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.CyberGreen
import com.example.ui.theme.CyberNavyBorder
import com.example.ui.theme.CyberPurple
import com.example.ui.theme.CyberPurpleDark
import com.example.ui.theme.CyberPurpleLight
import com.example.ui.theme.CyberRed
import com.example.ui.theme.DarkBackgroundObsidian
import com.example.ui.theme.DarkBorderSubtle
import com.example.ui.theme.DarkSurfaceCard
import com.example.ui.theme.DarkSurfaceCardElevated
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.viewmodel.CyberGuardUiState

@Composable
fun CommandScreen(
    uiState: CyberGuardUiState,
    onToggleGuarding: () -> Unit,
    onToggleVpn: () -> Unit,
    onToggleFirewall: () -> Unit,
    onToggleProtection: () -> Unit,
    onToggleNightVision: () -> Unit,
    onSimulateBreach: () -> Unit,
    onDismissBreach: () -> Unit,
    onToggleGeoAutoConnect: () -> Unit,
    onUpdateLocation: (Double, Double) -> Unit,
    onSimulateLocationPreset: (String) -> Unit,
    onToggleMuteAlerts: () -> Unit,
    onSetSeverityFilter: (EventSeverity?) -> Unit,
    onSelectAlertDetail: (SecurityEvent?) -> Unit,
    onDismissAlert: (String) -> Unit,
    onClearAllAlerts: () -> Unit,
    onToggleDisableAlertRule: (String) -> Unit,
    onTriggerSecurityError: (String) -> Unit,
    onNavigateToTriage: () -> Unit,
    onNavigateToMissions: () -> Unit,
    onToggleProxyServer: () -> Unit = {},
    onSetProxyPort: (Int) -> Unit = {},
    onTestProxyProbe: (Boolean) -> Unit = {},
    onDismissProxyTestResult: () -> Unit = {},
    onToggleDeviceCamera: (Boolean?) -> Unit = {},
    onToggleCameraFacing: () -> Unit = {},
    onSetCameraPermission: (Boolean) -> Unit = {},
    onTogglePrivacyMasking: () -> Unit = {},
    onTogglePrivacyMask: (String) -> Unit = {},
    onSetPrivacyMaskStyle: (PrivacyMaskStyle) -> Unit = {},
    onSelectPrivacyMaskForDetail: (PrivacyMask?) -> Unit = {},
    onAddCustomPrivacyMask: (String) -> Unit = {},
    onResetDefaultPrivacyMasks: () -> Unit = {},
    onToggleClarityFilter: () -> Unit = {},
    onTriggerRefocusPulse: () -> Unit = {},
    onToggleBlurDiagnostic: (Boolean?) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val locationService = remember(context) { LocationService(context) }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        onSetCameraPermission(isGranted)
        if (isGranted) {
            onToggleDeviceCamera(true)
        }
    }

    LaunchedEffect(Unit) {
        val hasCamPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
        onSetCameraPermission(hasCamPermission)
    }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val fineGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
        val coarseGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (fineGranted || coarseGranted) {
            locationService.getCurrentCoordinates { coords ->
                onUpdateLocation(coords.latitude, coords.longitude)
            }
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(DarkBackgroundObsidian)
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 96.dp)
        ) {
            // 1. Top Status Header (Systems Online + Time + Notifications)
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(CyberGreen)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "SYSTEMS ONLINE",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            color = CyberGreen
                        )
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "LIVE UTC",
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace,
                            color = TextMuted
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        IconButton(
                            onClick = onNavigateToTriage,
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = if (uiState.areAlertsMuted) Icons.Default.NotificationsOff else Icons.Default.Notifications,
                                contentDescription = "Alerts",
                                tint = if (uiState.areAlertsMuted) CyberRed else if (uiState.agentProfile.pendingAlerts > 0) CyberAmber else TextSecondary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            // 2. User Welcome & Pro Tag
            item {
                Column {
                    Text(
                        text = "Welcome back, ${uiState.agentProfile.title}",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = uiState.agentProfile.name,
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Surface(
                            color = CyberGreen.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(12.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, CyberGreen)
                        ) {
                            Text(
                                text = uiState.agentProfile.badge,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = CyberGreen,
                                fontFamily = FontFamily.Monospace,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // 3. Three Stat Boxes: Level | Total XP | Streak (From Screenshot 1)
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    StatTile(
                        title = "LEVEL",
                        value = "${uiState.agentProfile.level}",
                        subtitle = "${uiState.agentProfile.totalXp}/${uiState.agentProfile.nextLevelXp} XP",
                        modifier = Modifier.weight(1f)
                    )

                    StatTile(
                        title = "TOTAL XP",
                        value = "${uiState.agentProfile.totalXp}",
                        subtitle = "POINTS EARNED",
                        isHighlighted = true,
                        modifier = Modifier.weight(1f)
                    )

                    StatTile(
                        title = "STREAK",
                        value = "${uiState.agentProfile.streak}",
                        subtitle = "🔥 ON FIRE!",
                        subtitleColor = CyberAmber,
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(modifier = Modifier.height(18.dp))
            }

            // 4. Priority Alert Card: Daily Directive (From Screenshot 1)
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("priority_directive_card"),
                    colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
                    border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorderSubtle),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        val hasPending = uiState.agentProfile.pendingAlerts > 0
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = if (hasPending) Icons.Default.Warning else Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = if (hasPending) CyberAmber else CyberGreen,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (hasPending) "PRIORITY ALERT" else "ALL ALERTS RESOLVED",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (hasPending) CyberAmber else CyberGreen,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                            Surface(
                                color = if (hasPending) CyberAmber.copy(alpha = 0.2f) else CyberGreen.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = if (hasPending) "NEW" else "CLEARED",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (hasPending) CyberAmber else CyberGreen,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = if (hasPending) "Daily Directive" else "Perimeter Queue Pristine",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (hasPending)
                                "You have ${uiState.agentProfile.pendingAlerts} pending security alerts in your queue. Triage them to maintain system integrity and earn XP."
                            else
                                "All security alerts and errors have been cleared. Surveillance sensors report zero pending breaches.",
                            fontSize = 12.sp,
                            color = TextSecondary,
                            lineHeight = 17.sp
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = onNavigateToTriage,
                                modifier = Modifier
                                    .weight(1f)
                                    .height(44.dp)
                                    .testTag("start_triage_btn"),
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                                contentPadding = PaddingValues(0.dp),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(
                                            Brush.horizontalGradient(
                                                listOf(CyberPurpleDark, CyberPurple, CyberCyan)
                                            )
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = if (hasPending) "Start Triage" else "Open Triage Console",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp,
                                            color = Color.White
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Icon(
                                            imageVector = Icons.Default.ChevronRight,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            }

                            if (hasPending) {
                                OutlinedButton(
                                    onClick = onClearAllAlerts,
                                    modifier = Modifier.height(44.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = CyberRed),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, CyberRed.copy(alpha = 0.5f)),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.DeleteSweep,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Clear",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
            }

            // 5. GEOLOCATION CCTV RADAR & AUTOMATIC CONNECTION CARD
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("geo_location_card"),
                    colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CyberCyan.copy(alpha = 0.4f)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.MyLocation,
                                    contentDescription = null,
                                    tint = CyberCyan,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = "Geo-Location Auto-Connect",
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = TextPrimary
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Surface(
                                            color = CyberGreen.copy(alpha = 0.15f),
                                            shape = RoundedCornerShape(4.dp),
                                            border = androidx.compose.foundation.BorderStroke(1.dp, CyberGreen.copy(alpha = 0.5f))
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Box(
                                                    modifier = Modifier
                                                        .size(6.dp)
                                                        .clip(CircleShape)
                                                        .background(CyberGreen)
                                                )
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text(
                                                    text = "24/7 ACTIVE",
                                                    fontSize = 8.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    fontFamily = FontFamily.Monospace,
                                                    color = CyberGreen
                                                )
                                            }
                                        }
                                    }
                                    Text(
                                        text = "Play Services 21.3.0 Fused link • Real-time camera discovery",
                                        fontSize = 11.sp,
                                        color = TextMuted
                                    )
                                }
                            }

                            Switch(
                                checked = uiState.isGeoAutoConnectEnabled,
                                onCheckedChange = { onToggleGeoAutoConnect() },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = CyberCyan,
                                    checkedTrackColor = CyberCyan.copy(alpha = 0.3f),
                                    uncheckedThumbColor = TextMuted,
                                    uncheckedTrackColor = DarkSurfaceCardElevated
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        GeoCameraRadarView(
                            cameras = uiState.cameraZones,
                            activeZoneIndex = uiState.selectedZoneIndex,
                            isAutoConnectActive = uiState.isGeoAutoConnectEnabled
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            color = Color(0x3300E5FF),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.NearMe,
                                    contentDescription = null,
                                    tint = CyberCyan,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = uiState.geoStatusMessage,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.Monospace,
                                        color = TextPrimary
                                    )
                                    Text(
                                        text = "24/7 LIVE STREAM: ${uiState.userLatitude}° N, ${uiState.userLongitude}° W (Play Services 21.3.0)",
                                        fontSize = 9.sp,
                                        fontFamily = FontFamily.Monospace,
                                        color = CyberCyan
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "TEST GEOFENCE PROXIMITY (SIMULATE LOCATION):",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            color = TextSecondary
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            FilterChip(
                                selected = uiState.selectedZoneIndex == 0,
                                onClick = { onSimulateLocationPreset("ZONE_1") },
                                label = { Text("Gate (Zone 1)", fontSize = 10.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = CyberCyan.copy(alpha = 0.2f),
                                    selectedLabelColor = CyberCyan
                                )
                            )
                            FilterChip(
                                selected = uiState.selectedZoneIndex == 1,
                                onClick = { onSimulateLocationPreset("ZONE_2") },
                                label = { Text("Vault (Zone 2)", fontSize = 10.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = CyberCyan.copy(alpha = 0.2f),
                                    selectedLabelColor = CyberCyan
                                )
                            )
                            FilterChip(
                                selected = uiState.selectedZoneIndex == 2,
                                onClick = { onSimulateLocationPreset("ZONE_3") },
                                label = { Text("Dock (Zone 3)", fontSize = 10.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = CyberCyan.copy(alpha = 0.2f),
                                    selectedLabelColor = CyberCyan
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedButton(
                            onClick = {
                                if (locationService.hasLocationPermission()) {
                                    locationService.getCurrentCoordinates { coords ->
                                        onUpdateLocation(coords.latitude, coords.longitude)
                                    }
                                } else {
                                    locationPermissionLauncher.launch(
                                        arrayOf(
                                            Manifest.permission.ACCESS_FINE_LOCATION,
                                            Manifest.permission.ACCESS_COARSE_LOCATION
                                        )
                                    )
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = CyberCyan),
                            border = androidx.compose.foundation.BorderStroke(1.dp, CyberCyan.copy(alpha = 0.6f)),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.GpsFixed,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Sync Device GPS Location",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
            }

            // 6. Circular Perimeter Protection Gauge
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("circular_gauge_card"),
                    colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
                    border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorderSubtle),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Shield,
                                    contentDescription = null,
                                    tint = CyberCyan,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Perimeter Protected",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                            }

                            Surface(
                                color = if (uiState.isGuardingActive) CyberGreen.copy(alpha = 0.15f) else Color.DarkGray,
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = if (uiState.isGuardingActive) "ACTIVE GUARD" else "STANDBY",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (uiState.isGuardingActive) CyberGreen else TextMuted,
                                    fontFamily = FontFamily.Monospace,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        CircularScoreGauge(
                            score = uiState.overallScore(),
                            maxScore = 100,
                            subtitle = "Defensive Integrity Scanned"
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            DefenseToggleTile(
                                icon = Icons.Default.Wifi,
                                label = "VPN / SRTP",
                                status = if (uiState.vpnActive) "Active" else "OFF",
                                isActive = uiState.vpnActive,
                                onClick = onToggleVpn,
                                modifier = Modifier.weight(1f)
                            )
                            DefenseToggleTile(
                                icon = Icons.Default.LocalFireDepartment,
                                label = "Firewall",
                                status = if (uiState.firewallActive) "ON" else "OFF",
                                isActive = uiState.firewallActive,
                                onClick = onToggleFirewall,
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            DefenseToggleTile(
                                icon = Icons.Default.CheckCircle,
                                label = "Protection",
                                status = if (uiState.protectionActive) "Running" else "OFF",
                                isActive = uiState.protectionActive,
                                onClick = onToggleProtection,
                                modifier = Modifier.weight(1f)
                            )
                            DefenseToggleTile(
                                icon = Icons.Default.Router,
                                label = "Proxy Gateway",
                                status = if (uiState.isProxyServerActive) ":${uiState.proxyPort} ON" else "OFF",
                                isActive = uiState.isProxyServerActive,
                                onClick = onToggleProxyServer,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
            }

            // 7. Primary Action Button: Start Guarding / Disarm
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        onClick = onToggleGuarding,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("start_btn"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (uiState.isGuardingActive) CyberCyan else DarkSurfaceCardElevated,
                            contentColor = if (uiState.isGuardingActive) Color(0xFF00363D) else Color.White
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            imageVector = if (uiState.isGuardingActive) Icons.Default.Shield else Icons.Default.Security,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (uiState.isGuardingActive) "Start Guarding: Armed" else "Start Guarding (Standby)",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Protect your space from unauthorized eyes.",
                        fontSize = 12.sp,
                        color = TextMuted,
                        modifier = Modifier.testTag("instructional_text")
                    )
                    Spacer(modifier = Modifier.height(18.dp))
                }
            }

            // 8. Live CCTV Feed HUD
            item {
                Column(modifier = Modifier.fillMaxWidth()) {
                    val currentCam = uiState.cameraZones[uiState.selectedZoneIndex]
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = if (uiState.isDeviceCameraActive) "Device Camera CCTV Terminal" else "Auto-Connected Camera Feed",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = if (uiState.isDeviceCameraActive)
                                    "Local Hardware Sensor • ${if (uiState.cameraFacingFront) "Front Cam" else "Rear Cam"} Active"
                                else
                                    "${currentCam.name} • ${currentCam.distanceMeters.toInt()}m from user",
                                fontSize = 11.sp,
                                color = CyberGreen,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                        Surface(
                            color = if (uiState.isDeviceCameraActive) CyberCyan.copy(alpha = 0.15f) else CyberGreen.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = if (uiState.isDeviceCameraActive) "LOCAL NODE" else "AUTO-LOCK",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (uiState.isDeviceCameraActive) CyberCyan else CyberGreen,
                                fontFamily = FontFamily.Monospace,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Stream Source Selector: Remote CCTV vs Device Camera
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected = !uiState.isDeviceCameraActive,
                            onClick = { onToggleDeviceCamera(false) },
                            label = { Text("Remote CCTV Feeds", fontSize = 11.sp) },
                            leadingIcon = {
                                Icon(imageVector = Icons.Default.Security, contentDescription = null, modifier = Modifier.size(14.dp))
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = CyberCyan.copy(alpha = 0.2f),
                                selectedLabelColor = CyberCyan,
                                selectedLeadingIconColor = CyberCyan,
                                containerColor = DarkSurfaceCard,
                                labelColor = TextSecondary
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = !uiState.isDeviceCameraActive,
                                borderColor = DarkBorderSubtle,
                                selectedBorderColor = CyberCyan
                            ),
                            modifier = Modifier.weight(1f)
                        )

                        FilterChip(
                            selected = uiState.isDeviceCameraActive,
                            onClick = {
                                onToggleDeviceCamera(true)
                                val hasPerm = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                                if (hasPerm) {
                                    onSetCameraPermission(true)
                                } else {
                                    cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                                }
                            },
                            label = { Text("Device CCTV Cam", fontSize = 11.sp) },
                            leadingIcon = {
                                Icon(imageVector = Icons.Default.Videocam, contentDescription = null, modifier = Modifier.size(14.dp))
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = CyberCyan.copy(alpha = 0.2f),
                                selectedLabelColor = CyberCyan,
                                selectedLeadingIconColor = CyberCyan,
                                containerColor = DarkSurfaceCard,
                                labelColor = TextSecondary
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = uiState.isDeviceCameraActive,
                                borderColor = DarkBorderSubtle,
                                selectedBorderColor = CyberCyan
                            ),
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Dedicated CCTV Camera Hardware Permission HUD Card
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("cctv_camera_permission_hud_card"),
                        colors = CardDefaults.cardColors(
                            containerColor = if (uiState.isCameraPermissionGranted) DarkSurfaceCard else DarkSurfaceCardElevated
                        ),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (uiState.isCameraPermissionGranted) CyberGreen.copy(alpha = 0.5f) else CyberCyan.copy(alpha = 0.6f)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(36.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(if (uiState.isCameraPermissionGranted) CyberGreen.copy(alpha = 0.15f) else CyberCyan.copy(alpha = 0.15f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = if (uiState.isCameraPermissionGranted) Icons.Default.Videocam else Icons.Default.VideocamOff,
                                            contentDescription = "CCTV Hardware Access",
                                            tint = if (uiState.isCameraPermissionGranted) CyberGreen else CyberCyan,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(
                                            text = "CCTV Camera Hardware Node",
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = TextPrimary
                                        )
                                        Text(
                                            text = if (uiState.isCameraPermissionGranted)
                                                "PERMISSION: GRANTED • HARDWARE NODE ACTIVE"
                                            else
                                                "PERMISSION: REQUIRED • ACCESS PENDING",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = FontFamily.Monospace,
                                            color = if (uiState.isCameraPermissionGranted) CyberGreen else CyberAmber
                                        )
                                    }
                                }

                                Surface(
                                    color = if (uiState.isCameraPermissionGranted) CyberGreen.copy(alpha = 0.15f) else CyberAmber.copy(alpha = 0.15f),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = if (uiState.isCameraPermissionGranted) "AUTHORIZED" else "STANDBY",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.Monospace,
                                        color = if (uiState.isCameraPermissionGranted) CyberGreen else CyberAmber,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = if (uiState.isCameraPermissionGranted)
                                    "Camera hardware pipeline linked. Live stream is protected with SRTP TLS 1.3 encryption, privacy masks, and real-time reticle tracking."
                                else
                                    "Grant camera hardware access to turn this smartphone into an encrypted, live CCTV surveillance terminal node.",
                                fontSize = 11.sp,
                                color = TextSecondary,
                                lineHeight = 15.sp
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Button(
                                    onClick = {
                                        onSetCameraPermission(true)
                                        onToggleDeviceCamera(true)
                                        cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                                    },
                                    modifier = Modifier
                                        .weight(1.3f)
                                        .testTag("hud_card_grant_camera_btn"),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (uiState.isCameraPermissionGranted) CyberCyan.copy(alpha = 0.25f) else CyberCyan,
                                        contentColor = if (uiState.isCameraPermissionGranted) CyberCyan else Color(0xFF00363D)
                                    ),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Videocam,
                                        contentDescription = null,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = if (uiState.isCameraPermissionGranted) "CCTV Terminal Active" else "Grant Camera Permission",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                if (uiState.isCameraPermissionGranted) {
                                    OutlinedButton(
                                        onClick = onToggleCameraFacing,
                                        modifier = Modifier
                                            .weight(1f)
                                            .testTag("hud_card_switch_lens_btn"),
                                        colors = ButtonDefaults.outlinedButtonColors(contentColor = CyberCyan),
                                        border = androidx.compose.foundation.BorderStroke(1.dp, CyberCyan.copy(alpha = 0.5f)),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Cameraswitch,
                                            contentDescription = null,
                                            modifier = Modifier.size(13.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = if (uiState.cameraFacingFront) "Front Cam" else "Rear Cam",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                } else {
                                    OutlinedButton(
                                        onClick = {
                                            onSetCameraPermission(true)
                                            onToggleDeviceCamera(true)
                                        },
                                        modifier = Modifier
                                            .weight(1f)
                                            .testTag("hud_card_enable_cctv_btn"),
                                        colors = ButtonDefaults.outlinedButtonColors(contentColor = CyberGreen),
                                        border = androidx.compose.foundation.BorderStroke(1.dp, CyberGreen.copy(alpha = 0.5f)),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text("Enable Access", fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    CameraMonitorView(
                        cameraZone = currentCam,
                        isGuardingActive = uiState.isGuardingActive,
                        isNightVision = uiState.isNightVision,
                        simulatedBreach = uiState.isBreachSimulated,
                        isDeviceCameraActive = uiState.isDeviceCameraActive,
                        hasCameraPermission = uiState.isCameraPermissionGranted,
                        isFrontCamera = uiState.cameraFacingFront,
                        privacyMasks = uiState.privacyMasks,
                        isPrivacyMaskingEnabled = uiState.isPrivacyMaskingEnabled,
                        privacyMaskStyle = uiState.privacyMaskStyle,
                        isClarityFilterActive = uiState.isClarityFilterActive,
                        refocusPulseCount = uiState.refocusPulseCount,
                        onMaskClicked = { mask -> onSelectPrivacyMaskForDetail(mask) },
                        onRequestCameraPermission = {
                            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                        },
                        onToggleCameraFacing = onToggleCameraFacing,
                        onTriggerRefocus = onTriggerRefocusPulse,
                        onOpenBlurDiagnostic = { onToggleBlurDiagnostic(true) }
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Primary Stream Mode Row: IR Night, Lens Switch, Intruder Simulation
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = onToggleNightVision,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (uiState.isNightVision) CyberGreen.copy(alpha = 0.2f) else DarkSurfaceCard,
                                contentColor = if (uiState.isNightVision) CyberGreen else TextSecondary
                            ),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (uiState.isNightVision) CyberGreen else DarkBorderSubtle
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(text = if (uiState.isNightVision) "IR Night: ON" else "IR Night: OFF", fontSize = 11.sp)
                        }

                        if (uiState.isDeviceCameraActive && uiState.isCameraPermissionGranted) {
                            Button(
                                onClick = onToggleCameraFacing,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = DarkSurfaceCardElevated,
                                    contentColor = CyberCyan
                                ),
                                border = androidx.compose.foundation.BorderStroke(1.dp, CyberCyan.copy(alpha = 0.5f)),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Cameraswitch,
                                    contentDescription = null,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (uiState.cameraFacingFront) "Lens: Front" else "Lens: Rear",
                                    fontSize = 11.sp
                                )
                            }
                        }

                        Button(
                            onClick = {
                                if (uiState.isBreachSimulated) onDismissBreach() else onSimulateBreach()
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (uiState.isBreachSimulated) CyberRed else DarkSurfaceCardElevated,
                                contentColor = if (uiState.isBreachSimulated) Color.White else CyberRed
                            ),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (uiState.isBreachSimulated) CyberRed else CyberRed.copy(alpha = 0.5f)
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1.3f)
                        ) {
                            Icon(
                                imageVector = if (uiState.isBreachSimulated) Icons.Default.Close else Icons.Default.Warning,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (uiState.isBreachSimulated) "Dismiss Intruder" else "Simulate Intruder",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Secondary Optical & Clarity Enhancement Row (Refocus, Clarity Filter, Blur Diagnostic Guide)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = onTriggerRefocusPulse,
                            modifier = Modifier
                                .weight(1f)
                                .testTag("refocus_lens_btn"),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = DarkSurfaceCardElevated,
                                contentColor = CyberCyan
                            ),
                            border = androidx.compose.foundation.BorderStroke(1.dp, CyberCyan.copy(alpha = 0.5f)),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CenterFocusStrong,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Refocus Lens", fontSize = 11.sp)
                        }

                        Button(
                            onClick = onToggleClarityFilter,
                            modifier = Modifier
                                .weight(1.1f)
                                .testTag("clarity_filter_btn"),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (uiState.isClarityFilterActive) CyberCyan.copy(alpha = 0.2f) else DarkSurfaceCardElevated,
                                contentColor = if (uiState.isClarityFilterActive) CyberCyan else TextSecondary
                            ),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (uiState.isClarityFilterActive) CyberCyan else DarkBorderSubtle
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoFixHigh,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (uiState.isClarityFilterActive) "Clarity: ON" else "Clarity: OFF",
                                fontSize = 11.sp
                            )
                        }

                        OutlinedButton(
                            onClick = { onToggleBlurDiagnostic(true) },
                            modifier = Modifier
                                .weight(0.9f)
                                .testTag("blur_troubleshoot_btn"),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = CyberAmber),
                            border = androidx.compose.foundation.BorderStroke(1.dp, CyberAmber.copy(alpha = 0.5f)),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Blur Guide", fontSize = 11.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = if (uiState.isBreachSimulated) "Perimeter breach simulation active! Tap 'Dismiss Intruder' to clear." else "Tap feed or 'Refocus Lens' to autofocus. Toggle 'Clarity' for anti-blur sharpening.",
                        fontSize = 10.sp,
                        color = if (uiState.isBreachSimulated) CyberRed else TextMuted
                    )
                    Spacer(modifier = Modifier.height(18.dp))
                }
            }

            // 8A. CCTV PRIVACY ZONE MASKING CARD
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("privacy_masking_card"),
                    colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (uiState.isPrivacyMaskingEnabled) CyberCyan.copy(alpha = 0.5f) else DarkBorderSubtle
                    ),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (uiState.isPrivacyMaskingEnabled) CyberCyan.copy(alpha = 0.15f) else DarkSurfaceCardElevated),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.VisibilityOff,
                                        contentDescription = "Privacy Masking",
                                        tint = if (uiState.isPrivacyMaskingEnabled) CyberCyan else TextMuted,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = "CCTV Privacy Zone Masking",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                    Text(
                                        text = if (uiState.isPrivacyMaskingEnabled)
                                            "ACTIVE • ${uiState.privacyMasks.count { it.isEnabled }} ZONES OBSCURED"
                                        else
                                            "DISABLED • FEED FULLY EXPOSED",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.Monospace,
                                        color = if (uiState.isPrivacyMaskingEnabled) CyberGreen else CyberAmber
                                    )
                                }
                            }

                            Switch(
                                checked = uiState.isPrivacyMaskingEnabled,
                                onCheckedChange = { onTogglePrivacyMasking() },
                                modifier = Modifier.testTag("privacy_mask_switch"),
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = CyberCyan,
                                    checkedTrackColor = CyberCyan.copy(alpha = 0.3f),
                                    uncheckedThumbColor = TextMuted,
                                    uncheckedTrackColor = DarkSurfaceCardElevated
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "Redacts or blacks out sensitive spatial regions on the camera stream (such as PIN entry keypads, workstation monitors, or residential windows) to comply with workplace privacy laws.",
                            fontSize = 11.sp,
                            color = TextSecondary,
                            lineHeight = 16.sp
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Mask Style Selector
                        Text(
                            text = "MASK REDACTION STYLE:",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            color = TextMuted
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            PrivacyMaskStyle.entries.forEach { style ->
                                val isSelected = uiState.privacyMaskStyle == style
                                val label = when (style) {
                                    PrivacyMaskStyle.BLACKOUT -> "Blackout"
                                    PrivacyMaskStyle.REDACTION_STRIPES -> "Stripes"
                                    PrivacyMaskStyle.PIXEL_MOSAIC -> "Mosaic"
                                }
                                Surface(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(6.dp))
                                        .clickable { onSetPrivacyMaskStyle(style) },
                                    color = if (isSelected) CyberCyan.copy(alpha = 0.2f) else DarkSurfaceCardElevated,
                                    border = androidx.compose.foundation.BorderStroke(
                                        1.dp,
                                        if (isSelected) CyberCyan else DarkBorderSubtle
                                    ),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = label,
                                        fontSize = 10.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        fontFamily = FontFamily.Monospace,
                                        color = if (isSelected) CyberCyan else TextSecondary,
                                        modifier = Modifier.padding(vertical = 6.dp),
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Active Privacy Masks / Zones
                        Text(
                            text = "CONFIGURED SENSITIVE ZONES:",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            color = TextMuted
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            uiState.privacyMasks.forEach { mask ->
                                Surface(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(8.dp))
                                        .clickable { onSelectPrivacyMaskForDetail(mask) },
                                    color = DarkSurfaceCardElevated,
                                    border = androidx.compose.foundation.BorderStroke(
                                        1.dp,
                                        if (mask.isEnabled) CyberCyan.copy(alpha = 0.4f) else DarkBorderSubtle
                                    ),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Icon(
                                                imageVector = if (mask.isEnabled) Icons.Default.Lock else Icons.Default.Visibility,
                                                contentDescription = null,
                                                tint = if (mask.isEnabled) CyberCyan else TextMuted,
                                                modifier = Modifier.size(16.dp)
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Column {
                                                Text(
                                                    text = mask.label,
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.SemiBold,
                                                    color = if (mask.isEnabled) TextPrimary else TextMuted
                                                )
                                                Text(
                                                    text = mask.description,
                                                    fontSize = 9.sp,
                                                    color = TextMuted,
                                                    maxLines = 1
                                                )
                                            }
                                        }

                                        Switch(
                                            checked = mask.isEnabled,
                                            onCheckedChange = { onTogglePrivacyMask(mask.id) },
                                            colors = SwitchDefaults.colors(
                                                checkedThumbColor = CyberCyan,
                                                checkedTrackColor = CyberCyan.copy(alpha = 0.3f),
                                                uncheckedThumbColor = TextMuted,
                                                uncheckedTrackColor = DarkSurfaceCard
                                            )
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Quick Actions: Add Custom Mask & Reset
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedButton(
                                onClick = {
                                    onAddCustomPrivacyMask("Custom Zone ${uiState.privacyMasks.size + 1}")
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("add_privacy_mask_btn"),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = CyberCyan),
                                border = androidx.compose.foundation.BorderStroke(1.dp, CyberCyan.copy(alpha = 0.6f)),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("+ Add Custom Mask", fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
                            }

                            OutlinedButton(
                                onClick = onResetDefaultPrivacyMasks,
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("reset_privacy_masks_btn"),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = TextSecondary),
                                border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorderSubtle),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Reset Defaults", fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
                            }
                        }

                        // Selected Mask Details Dialog / Banner
                        if (uiState.selectedPrivacyMaskForDetail != null) {
                            val selMask = uiState.selectedPrivacyMaskForDetail
                            Spacer(modifier = Modifier.height(10.dp))
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                color = CyberCyan.copy(alpha = 0.12f),
                                shape = RoundedCornerShape(8.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, CyberCyan.copy(alpha = 0.5f))
                            ) {
                                Row(
                                    modifier = Modifier.padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = "SELECTED: ${selMask.label.uppercase()}",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = FontFamily.Monospace,
                                            color = CyberCyan
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = selMask.description,
                                            fontSize = 10.sp,
                                            color = TextSecondary
                                        )
                                        Text(
                                            text = "Position: ${(selMask.leftRatio * 100).toInt()}%L, ${(selMask.topRatio * 100).toInt()}%T • Size: ${(selMask.widthRatio * 100).toInt()}%W x ${(selMask.heightRatio * 100).toInt()}%H",
                                            fontSize = 9.sp,
                                            fontFamily = FontFamily.Monospace,
                                            color = TextMuted
                                        )
                                    }

                                    IconButton(
                                        onClick = { onSelectPrivacyMaskForDetail(null) },
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = "Close Detail",
                                            tint = TextMuted,
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(18.dp))
            }

            // 8B. DEFENSIVE CCTV PROXY GATEWAY CARD
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("proxy_gateway_card"),
                    colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (uiState.isProxyServerActive) CyberCyan.copy(alpha = 0.5f) else DarkBorderSubtle
                    ),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (uiState.isProxyServerActive) CyberCyan.copy(alpha = 0.15f) else DarkSurfaceCardElevated),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Router,
                                        contentDescription = "Proxy Gateway",
                                        tint = if (uiState.isProxyServerActive) CyberCyan else TextMuted,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = "Defensive Proxy Gateway",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                    Text(
                                        text = if (uiState.isProxyServerActive) "LISTENING ${uiState.proxyHost}:${uiState.proxyPort}" else "OFFLINE • STREAMS UNFILTERED",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.Monospace,
                                        color = if (uiState.isProxyServerActive) CyberGreen else CyberRed
                                    )
                                }
                            }

                            Switch(
                                checked = uiState.isProxyServerActive,
                                onCheckedChange = { onToggleProxyServer() },
                                modifier = Modifier.testTag("proxy_switch"),
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = CyberCyan,
                                    checkedTrackColor = CyberCyan.copy(alpha = 0.3f),
                                    uncheckedThumbColor = TextMuted,
                                    uncheckedTrackColor = DarkSurfaceCardElevated
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "Inspects incoming CCTV RTSP & HTTP streaming payloads, quarantining path traversals (../), default credentials (admin:admin), and exploit commands on localhost.",
                            fontSize = 11.sp,
                            color = TextSecondary,
                            lineHeight = 16.sp
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Port Selection Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "PORT:",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                color = TextMuted,
                                modifier = Modifier.width(50.dp)
                            )

                            Row(
                                modifier = Modifier.weight(1f),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                val ports = listOf(8080, 8443, 3128, 8888)
                                ports.forEach { port ->
                                    val isSelected = uiState.proxyPort == port
                                    Surface(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(6.dp))
                                            .clickable { onSetProxyPort(port) },
                                        color = if (isSelected) CyberCyan.copy(alpha = 0.2f) else DarkSurfaceCardElevated,
                                        border = androidx.compose.foundation.BorderStroke(
                                            1.dp,
                                            if (isSelected) CyberCyan else DarkBorderSubtle
                                        ),
                                        shape = RoundedCornerShape(6.dp)
                                    ) {
                                        Text(
                                            text = ":$port",
                                            fontSize = 10.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                            fontFamily = FontFamily.Monospace,
                                            color = if (isSelected) CyberCyan else TextSecondary,
                                            modifier = Modifier.padding(vertical = 6.dp),
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Proxy Metrics Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Surface(
                                modifier = Modifier.weight(1f),
                                color = DarkSurfaceCardElevated,
                                shape = RoundedCornerShape(8.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorderSubtle)
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Text(
                                        text = "REQUESTS INSPECTED",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.Monospace,
                                        color = TextMuted
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "${uiState.proxyRequestsInspected}",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        fontFamily = FontFamily.Monospace,
                                        color = CyberCyan
                                    )
                                }
                            }

                            Surface(
                                modifier = Modifier.weight(1f),
                                color = DarkSurfaceCardElevated,
                                shape = RoundedCornerShape(8.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorderSubtle)
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Text(
                                        text = "EXPLOITS QUARANTINED",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.Monospace,
                                        color = TextMuted
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "${uiState.proxyThreatsBlocked}",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        fontFamily = FontFamily.Monospace,
                                        color = if (uiState.proxyThreatsBlocked > 0) CyberAmber else CyberGreen
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Diagnostic Test Probe Buttons
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedButton(
                                onClick = { onTestProxyProbe(false) },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("test_benign_probe_btn"),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = CyberCyan),
                                border = androidx.compose.foundation.BorderStroke(1.dp, CyberCyan.copy(alpha = 0.6f)),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Test Benign Stream", fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
                            }

                            OutlinedButton(
                                onClick = { onTestProxyProbe(true) },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("test_exploit_probe_btn"),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = CyberRed),
                                border = androidx.compose.foundation.BorderStroke(1.dp, CyberRed.copy(alpha = 0.6f)),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Inject Exploit Probe", fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
                            }
                        }

                        // Test Result Banner
                        if (uiState.proxyTestResult != null) {
                            Spacer(modifier = Modifier.height(10.dp))
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                color = if (uiState.proxyTestResult.contains("DEFENSE SUCCESS") || uiState.proxyTestResult.contains("SUCCESS"))
                                    CyberGreen.copy(alpha = 0.15f)
                                else
                                    CyberRed.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(8.dp),
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (uiState.proxyTestResult.contains("DEFENSE SUCCESS") || uiState.proxyTestResult.contains("SUCCESS"))
                                        CyberGreen.copy(alpha = 0.6f)
                                    else
                                        CyberRed.copy(alpha = 0.6f)
                                )
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = uiState.proxyTestResult,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = TextPrimary,
                                        modifier = Modifier.weight(1f)
                                    )
                                    IconButton(
                                        onClick = onDismissProxyTestResult,
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = "Dismiss Result",
                                            tint = TextMuted,
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(18.dp))
            }

            // 9. ENHANCED ALERTS & THREAT LOG (WITH ERROR SIMULATOR & DISABLE CONTROLS)
            item {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Alerts & Threat Log",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Surface(
                                color = if (uiState.areAlertsMuted) CyberRed.copy(alpha = 0.2f) else CyberCyan.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = if (uiState.areAlertsMuted) "MUTED / DISABLED" else "${uiState.filteredEvents().size} ACTIVE",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace,
                                    color = if (uiState.areAlertsMuted) CyberRed else CyberCyan,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            // Mute/Disable Alerts Button
                            IconButton(
                                onClick = onToggleMuteAlerts,
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = if (uiState.areAlertsMuted) Icons.Default.NotificationsOff else Icons.Default.NotificationsActive,
                                    contentDescription = if (uiState.areAlertsMuted) "Unmute Alerts" else "Mute Alerts",
                                    tint = if (uiState.areAlertsMuted) CyberRed else CyberGreen,
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            // Clear All Logs Button
                            IconButton(
                                onClick = onClearAllAlerts,
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.DeleteSweep,
                                    contentDescription = "Clear All Logs",
                                    tint = TextMuted,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Severity Filter Chips (All, Errors, Warnings, Info)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        FilterChip(
                            selected = uiState.activeSeverityFilter == null,
                            onClick = { onSetSeverityFilter(null) },
                            label = { Text("All (${uiState.events.size})", fontSize = 10.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = CyberCyan.copy(alpha = 0.2f),
                                selectedLabelColor = CyberCyan
                            )
                        )
                        FilterChip(
                            selected = uiState.activeSeverityFilter == EventSeverity.CRITICAL,
                            onClick = { onSetSeverityFilter(EventSeverity.CRITICAL) },
                            label = { Text("Errors (${uiState.events.count { it.severity == EventSeverity.CRITICAL }})", fontSize = 10.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = CyberRed.copy(alpha = 0.2f),
                                selectedLabelColor = CyberRed
                            )
                        )
                        FilterChip(
                            selected = uiState.activeSeverityFilter == EventSeverity.WARNING,
                            onClick = { onSetSeverityFilter(EventSeverity.WARNING) },
                            label = { Text("Warnings (${uiState.events.count { it.severity == EventSeverity.WARNING }})", fontSize = 10.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = CyberAmber.copy(alpha = 0.2f),
                                selectedLabelColor = CyberAmber
                            )
                        )
                        FilterChip(
                            selected = uiState.activeSeverityFilter == EventSeverity.INFO,
                            onClick = { onSetSeverityFilter(EventSeverity.INFO) },
                            label = { Text("Info (${uiState.events.count { it.severity == EventSeverity.INFO }})", fontSize = 10.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = CyberGreen.copy(alpha = 0.2f),
                                selectedLabelColor = CyberGreen
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Trigger Live Security Error Bar
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = DarkSurfaceCard,
                        shape = RoundedCornerShape(10.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorderSubtle)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                text = "TRIGGER SECURITY ERROR ALERT (TEST DIAGNOSTICS):",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                color = CyberAmber
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                item {
                                    Button(
                                        onClick = { onTriggerSecurityError("BLUR_TAMPER") },
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0x33FFB300), contentColor = CyberAmber),
                                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                        shape = RoundedCornerShape(6.dp),
                                        modifier = Modifier.height(28.dp).testTag("trigger_blur_tamper_btn")
                                    ) {
                                        Text("👁️ Lens Defocus / Blur", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                                item {
                                    Button(
                                        onClick = { onTriggerSecurityError("CIPHER_ERROR") },
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0x33FF3D71), contentColor = CyberRed),
                                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                        shape = RoundedCornerShape(6.dp),
                                        modifier = Modifier.height(28.dp)
                                    ) {
                                        Text("🚨 Cipher Tamper", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                                item {
                                    Button(
                                        onClick = { onTriggerSecurityError("AUTH_ERROR") },
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0x33FF3D71), contentColor = CyberRed),
                                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                        shape = RoundedCornerShape(6.dp),
                                        modifier = Modifier.height(28.dp)
                                    ) {
                                        Text("🚨 Auth Lockout", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                                item {
                                    Button(
                                        onClick = { onTriggerSecurityError("CAM_OFFLINE") },
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0x33FFB300), contentColor = CyberAmber),
                                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                        shape = RoundedCornerShape(6.dp),
                                        modifier = Modifier.height(28.dp)
                                    ) {
                                        Text("⚠️ Cam Offline", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                                item {
                                    Button(
                                        onClick = { onTriggerSecurityError("INTRUDER") },
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0x33FF3D71), contentColor = CyberRed),
                                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                        shape = RoundedCornerShape(6.dp),
                                        modifier = Modifier.height(28.dp)
                                    ) {
                                        Text("🚨 Intruder Breach", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                }
            }

            // Display filtered event cards
            val eventsToShow = uiState.filteredEvents()
            if (eventsToShow.isEmpty()) {
                item {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        color = DarkSurfaceCard,
                        border = androidx.compose.foundation.BorderStroke(1.dp, if (uiState.events.isEmpty()) CyberGreen.copy(alpha = 0.3f) else DarkBorderSubtle),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            if (uiState.events.isEmpty()) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = CyberGreen,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                            }
                            Text(
                                text = when {
                                    uiState.areAlertsMuted -> "Alerts are currently MUTED / DISABLED. Tap the bell icon above to re-enable."
                                    uiState.events.isEmpty() -> "All Alerts & Threat Errors Cleared! Zero active vulnerabilities logged."
                                    else -> "No alerts matching selected filter."
                                },
                                fontSize = 12.sp,
                                color = if (uiState.events.isEmpty()) CyberGreen else TextMuted,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                }
            } else {
                items(eventsToShow.take(8)) { event ->
                    EnhancedAlertLogRow(
                        event = event,
                        onClick = { onSelectAlertDetail(event) }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }

        // Alert Diagnostics & Disabling Dialog
        if (uiState.selectedAlertForDetail != null) {
            val alert = uiState.selectedAlertForDetail
            AlertDialog(
                onDismissRequest = { onSelectAlertDetail(null) },
                containerColor = DarkSurfaceCard,
                titleContentColor = TextPrimary,
                textContentColor = TextSecondary,
                icon = {
                    Icon(
                        imageVector = if (alert.severity == EventSeverity.CRITICAL) Icons.Default.Warning else Icons.Default.Info,
                        contentDescription = null,
                        tint = if (alert.severity == EventSeverity.CRITICAL) CyberRed else if (alert.severity == EventSeverity.WARNING) CyberAmber else CyberCyan,
                        modifier = Modifier.size(28.dp)
                    )
                },
                title = {
                    Text(
                        text = alert.eventType,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                text = {
                    Column {
                        if (alert.errorCode != null) {
                            Surface(
                                color = Color(0x33FF3D71),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = "ERROR CODE: ${alert.errorCode}",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace,
                                    color = CyberRed,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                        }

                        Text(
                            text = "Camera: ${alert.cameraName} • ${alert.timestamp}",
                            fontSize = 12.sp,
                            color = CyberCyan
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = alert.description,
                            fontSize = 13.sp,
                            color = TextPrimary,
                            lineHeight = 18.sp
                        )

                        if (alert.remediationAction != null) {
                            Spacer(modifier = Modifier.height(10.dp))
                            Surface(
                                color = DarkSurfaceCardElevated,
                                shape = RoundedCornerShape(6.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(8.dp)) {
                                    Text(
                                        text = "RECOMMENDED ACTION:",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.Monospace,
                                        color = CyberGreen
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = alert.remediationAction,
                                        fontSize = 11.sp,
                                        color = TextSecondary
                                    )
                                }
                            }
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = { onDismissAlert(alert.id) },
                        colors = ButtonDefaults.buttonColors(containerColor = CyberCyan, contentColor = Color(0xFF00363D))
                    ) {
                        Text("Dismiss / Resolve", fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    Row {
                        OutlinedButton(
                            onClick = { onToggleDisableAlertRule(alert.eventType) },
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = CyberAmber),
                            border = androidx.compose.foundation.BorderStroke(1.dp, CyberAmber.copy(alpha = 0.5f))
                        ) {
                            Icon(imageVector = Icons.Default.Block, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Disable This Rule", fontSize = 11.sp)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        TextButton(onClick = { onSelectAlertDetail(null) }) {
                            Text("Close", color = TextSecondary)
                        }
                    }
                }
            )
        }

        // CCTV Blur & Defocus Diagnostic Troubleshooting Guide Dialog
        if (uiState.isBlurDiagnosticOpen) {
            AlertDialog(
                onDismissRequest = { onToggleBlurDiagnostic(false) },
                containerColor = DarkSurfaceCard,
                titleContentColor = TextPrimary,
                textContentColor = TextSecondary,
                icon = {
                    Icon(
                        imageVector = Icons.Default.CenterFocusStrong,
                        contentDescription = "Blur Guide",
                        tint = CyberCyan,
                        modifier = Modifier.size(32.dp)
                    )
                },
                title = {
                    Text(
                        text = "CCTV Blur & Defocus Diagnostics",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                },
                text = {
                    Column(
                        modifier = Modifier.padding(vertical = 4.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Surface(
                            color = DarkSurfaceCardElevated,
                            shape = RoundedCornerShape(8.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, CyberCyan.copy(alpha = 0.3f))
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(
                                    text = "1. AUTO-FOCUS RECALIBRATION",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace,
                                    color = CyberCyan
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "Tap anywhere on the live video stream or click 'Refocus Lens' below to force CameraX optical auto-focus and AE metering to sweep and lock.",
                                    fontSize = 11.sp,
                                    color = TextSecondary,
                                    lineHeight = 15.sp
                                )
                            }
                        }

                        Surface(
                            color = DarkSurfaceCardElevated,
                            shape = RoundedCornerShape(8.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorderSubtle)
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(
                                    text = "2. CONDENSATION & DOME SMUDGES",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace,
                                    color = CyberAmber
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "Temperature drops create dew-point condensation inside outdoor camera glass. IR LED illuminators at night can reflect off moisture, causing blooming and hazy blur.",
                                    fontSize = 11.sp,
                                    color = TextSecondary,
                                    lineHeight = 15.sp
                                )
                            }
                        }

                        Surface(
                            color = DarkSurfaceCardElevated,
                            shape = RoundedCornerShape(8.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorderSubtle)
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(
                                    text = "3. EMULATOR VIRTUAL CAMERA",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace,
                                    color = CyberGreen
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "In Android Studio and cloud emulators, the virtual camera generates a colorful checkerboard pattern. On physical devices, actual optical sensors and zoom lenses are engaged.",
                                    fontSize = 11.sp,
                                    color = TextSecondary,
                                    lineHeight = 15.sp
                                )
                            }
                        }

                        Surface(
                            color = DarkSurfaceCardElevated,
                            shape = RoundedCornerShape(8.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorderSubtle)
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(
                                    text = "4. ANTI-BLUR CLARITY SHARPENING",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace,
                                    color = CyberCyan
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "Toggle the 'Clarity' button to apply real-time contrast boost and edge-enhancement processing over blurry or low-light streams.",
                                    fontSize = 11.sp,
                                    color = TextSecondary,
                                    lineHeight = 15.sp
                                )
                            }
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            onTriggerRefocusPulse()
                            onToggleBlurDiagnostic(false)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CyberCyan,
                            contentColor = Color(0xFF00363D)
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.CenterFocusStrong,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Refocus Now", fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { onToggleBlurDiagnostic(false) }) {
                        Text("Close", color = TextSecondary)
                    }
                }
            )
        }
    }
}

@Composable
fun EnhancedAlertLogRow(
    event: SecurityEvent,
    onClick: () -> Unit
) {
    val iconColor = when (event.severity) {
        EventSeverity.INFO -> CyberCyan
        EventSeverity.WARNING -> CyberAmber
        EventSeverity.ALERT -> CyberAmber
        EventSeverity.CRITICAL -> CyberRed
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .clickable { onClick() },
        color = DarkSurfaceCard,
        shape = RoundedCornerShape(10.dp),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (event.severity == EventSeverity.CRITICAL) CyberRed.copy(alpha = 0.4f) else DarkBorderSubtle
        )
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(iconColor)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = event.eventType,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (event.severity == EventSeverity.CRITICAL) CyberRed else TextPrimary
                    )
                    Text(
                        text = event.timestamp,
                        fontSize = 10.sp,
                        color = TextMuted
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = event.cameraName,
                        fontSize = 11.sp,
                        color = TextSecondary
                    )
                    if (event.errorCode != null) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "[${event.errorCode}]",
                            fontSize = 10.sp,
                            fontFamily = FontFamily.Monospace,
                            color = CyberAmber
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.width(6.dp))
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Details",
                tint = TextMuted,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
fun StatTile(
    title: String,
    value: String,
    subtitle: String,
    isHighlighted: Boolean = false,
    subtitleColor: Color = TextMuted,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isHighlighted) CyberCyan.copy(alpha = 0.6f) else DarkBorderSubtle
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = title,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                color = if (isHighlighted) CyberCyan else TextMuted
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = FontFamily.Monospace,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                fontSize = 9.sp,
                fontWeight = FontWeight.SemiBold,
                color = subtitleColor
            )
        }
    }
}

@Composable
fun DefenseToggleTile(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    status: String,
    isActive: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .clickable { onClick() },
        color = DarkSurfaceCardElevated,
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isActive) CyberGreen.copy(alpha = 0.5f) else DarkBorderSubtle
        ),
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isActive) CyberGreen else TextMuted,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = label,
                fontSize = 10.sp,
                color = TextSecondary
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = status,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = if (isActive) CyberGreen else TextMuted
            )
        }
    }
}
