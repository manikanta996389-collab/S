package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.Radar
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Policy
import androidx.compose.material.icons.outlined.Radar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.data.LocationService
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.model.TriageDecision
import com.example.ui.screens.AuditScreen
import com.example.ui.screens.CommandScreen
import com.example.ui.screens.LessonsScreen
import com.example.ui.screens.MissionsScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.QuizScreen
import com.example.ui.screens.TriageTrainerScreen
import com.example.ui.theme.CCTVGuardTheme
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.DarkBorderSubtle
import com.example.ui.theme.DarkSurfaceCard
import com.example.ui.theme.TextMuted
import com.example.viewmodel.CyberGuardViewModel

enum class MainTab(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val testTag: String
) {
    COMMAND("Command", Icons.Filled.Home, Icons.Outlined.Home, "nav_command"),
    MISSIONS("Missions", Icons.Filled.Radar, Icons.Outlined.Radar, "nav_missions"),
    AUDIT("Audit", Icons.Filled.Policy, Icons.Outlined.Policy, "nav_audit"),
    PROFILE("Profile", Icons.Filled.Person, Icons.Outlined.Person, "nav_profile")
}

enum class ActiveSubScreen {
    NONE,
    TRIAGE_TRAINER,
    QUIZ,
    LESSONS
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CCTVGuardTheme {
                MainApp()
            }
        }
    }
}

@Composable
fun MainApp(
    viewModel: CyberGuardViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var currentTab by rememberSaveable { mutableStateOf(MainTab.COMMAND) }
    var activeSubScreen by rememberSaveable { mutableStateOf(ActiveSubScreen.NONE) }

    val context = LocalContext.current
    val locationService = remember(context) { LocationService(context) }

    // 24/7 active location stream for real-time camera discovery and geofence tracking
    LaunchedEffect(uiState.isGeoAutoConnectEnabled) {
        if (uiState.isGeoAutoConnectEnabled && locationService.hasLocationPermission()) {
            locationService.getLocationUpdates(intervalMs = 5000L).collect { coords ->
                viewModel.updateUserLocation(coords.latitude, coords.longitude)
            }
        }
    }

    // Handle back button behavior
    BackHandler(enabled = activeSubScreen != ActiveSubScreen.NONE || currentTab != MainTab.COMMAND) {
        if (activeSubScreen != ActiveSubScreen.NONE) {
            activeSubScreen = ActiveSubScreen.NONE
        } else {
            currentTab = MainTab.COMMAND
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (activeSubScreen == ActiveSubScreen.NONE) {
                NavigationBar(
                    containerColor = DarkSurfaceCard,
                    tonalElevation = 8.dp
                ) {
                    MainTab.entries.forEach { tab ->
                        val isSelected = currentTab == tab
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = {
                                activeSubScreen = ActiveSubScreen.NONE
                                currentTab = tab
                            },
                            icon = {
                                Icon(
                                    imageVector = if (isSelected) tab.selectedIcon else tab.unselectedIcon,
                                    contentDescription = tab.title
                                )
                            },
                            label = {
                                Text(
                                    text = tab.title,
                                    fontSize = 11.sp
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = CyberCyan,
                                selectedTextColor = CyberCyan,
                                unselectedIconColor = TextMuted,
                                unselectedTextColor = TextMuted,
                                indicatorColor = DarkBorderSubtle
                            ),
                            modifier = Modifier.testTag(tab.testTag)
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        when (activeSubScreen) {
            ActiveSubScreen.TRIAGE_TRAINER -> TriageTrainerScreen(
                uiState = uiState,
                onTriageDecision = { alertId, decision -> viewModel.triageAlert(alertId, decision) },
                onResetQueue = { viewModel.resetTriage() },
                onClearAllAlerts = { viewModel.clearAllTriageAlerts() },
                onRemoveAlert = { alertId -> viewModel.removeTriageAlert(alertId) },
                onReloadAlerts = { viewModel.reloadSampleTriageAlerts() },
                onBack = { activeSubScreen = ActiveSubScreen.NONE },
                modifier = Modifier.padding(innerPadding)
            )

            ActiveSubScreen.QUIZ -> QuizScreen(
                uiState = uiState,
                onAnswer = { qId: Int, optIdx: Int, isCorrect: Boolean ->
                    viewModel.answerQuestion(qId, optIdx, isCorrect)
                },
                onBack = { activeSubScreen = ActiveSubScreen.NONE },
                modifier = Modifier.padding(innerPadding)
            )

            ActiveSubScreen.LESSONS -> LessonsScreen(
                onBack = { activeSubScreen = ActiveSubScreen.NONE },
                modifier = Modifier.padding(innerPadding)
            )

            ActiveSubScreen.NONE -> {
                when (currentTab) {
                    MainTab.COMMAND -> CommandScreen(
                        uiState = uiState,
                        onToggleGuarding = { viewModel.toggleGuarding() },
                        onToggleVpn = { viewModel.toggleVpn() },
                        onToggleFirewall = { viewModel.toggleFirewall() },
                        onToggleProtection = { viewModel.toggleProtection() },
                        onToggleNightVision = { viewModel.toggleNightVision() },
                        onSimulateBreach = { viewModel.simulateBreach() },
                        onDismissBreach = { viewModel.dismissBreach() },
                        onToggleGeoAutoConnect = { viewModel.toggleGeoAutoConnect() },
                        onUpdateLocation = { lat, lon -> viewModel.updateUserLocation(lat, lon) },
                        onSimulateLocationPreset = { presetKey -> viewModel.simulateLocationPreset(presetKey) },
                        onToggleMuteAlerts = { viewModel.toggleMuteAlerts() },
                        onSetSeverityFilter = { sev -> viewModel.setSeverityFilter(sev) },
                        onSelectAlertDetail = { event -> viewModel.selectAlertDetail(event) },
                        onDismissAlert = { alertId -> viewModel.dismissAlert(alertId) },
                        onClearAllAlerts = { viewModel.clearAllAlerts() },
                        onToggleDisableAlertRule = { rule -> viewModel.toggleDisableAlertRule(rule) },
                        onTriggerSecurityError = { preset -> viewModel.triggerSecurityError(preset) },
                        onNavigateToTriage = { activeSubScreen = ActiveSubScreen.TRIAGE_TRAINER },
                        onNavigateToMissions = { currentTab = MainTab.MISSIONS },
                        onToggleProxyServer = { viewModel.toggleProxyServer() },
                        onSetProxyPort = { port -> viewModel.setProxyPort(port) },
                        onTestProxyProbe = { isMalicious -> viewModel.testProxyProbe(isMalicious) },
                        onDismissProxyTestResult = { viewModel.dismissProxyTestResult() },
                        onToggleDeviceCamera = { enable -> viewModel.toggleDeviceCamera(enable) },
                        onToggleCameraFacing = { viewModel.toggleCameraFacing() },
                        onSetCameraPermission = { granted -> viewModel.setCameraPermission(granted) },
                        onTogglePrivacyMasking = { viewModel.togglePrivacyMasking() },
                        onTogglePrivacyMask = { maskId -> viewModel.togglePrivacyMask(maskId) },
                        onSetPrivacyMaskStyle = { style -> viewModel.setPrivacyMaskStyle(style) },
                        onSelectPrivacyMaskForDetail = { mask -> viewModel.selectPrivacyMaskForDetail(mask) },
                        onAddCustomPrivacyMask = { label -> viewModel.addCustomPrivacyMask(label) },
                        onResetDefaultPrivacyMasks = { viewModel.resetDefaultPrivacyMasks() },
                        onToggleClarityFilter = { viewModel.toggleClarityFilter() },
                        onTriggerRefocusPulse = { viewModel.triggerRefocusPulse() },
                        onToggleBlurDiagnostic = { open -> viewModel.toggleBlurDiagnostic(open) },
                        onSetClipNamingPrefix = { prefix -> viewModel.setClipNamingPrefix(prefix) },
                        onSetClipNamingPattern = { pattern -> viewModel.setClipNamingPattern(pattern) },
                        onStartVideoRecording = { path -> viewModel.startVideoRecording(path) },
                        onUpdateRecordingProgress = { ms -> viewModel.updateRecordingProgress(ms) },
                        onStopVideoRecording = { viewModel.stopVideoRecording() },
                        onVideoRecordingSaved = { name, dur, clips, bytes ->
                            viewModel.onVideoRecordingSaved(name, dur, clips, bytes)
                        },
                        onVideoRecordingError = { msg -> viewModel.onVideoRecordingError(msg) },
                        onRefreshSavedVideoClips = { clips, bytes ->
                            viewModel.refreshSavedVideoClips(clips, bytes)
                        },
                        onSelectClipForPlayback = { clip -> viewModel.selectClipForPlayback(clip) },
                        modifier = Modifier.padding(innerPadding)
                    )

                    MainTab.MISSIONS -> MissionsScreen(
                        onOpenTriage = { activeSubScreen = ActiveSubScreen.TRIAGE_TRAINER },
                        onOpenQuiz = { activeSubScreen = ActiveSubScreen.QUIZ },
                        onOpenLessons = { activeSubScreen = ActiveSubScreen.LESSONS },
                        onOpenAudit = { currentTab = MainTab.AUDIT },
                        modifier = Modifier.padding(innerPadding)
                    )

                    MainTab.AUDIT -> AuditScreen(
                        uiState = uiState,
                        onFixPassword = { cameraId: String -> viewModel.fixAuditPassword(cameraId) },
                        onFixUPnP = { cameraId: String -> viewModel.fixAuditUPnP(cameraId) },
                        onFixFirmware = { cameraId: String -> viewModel.fixAuditFirmware(cameraId) },
                        onFixEncryption = { cameraId: String -> viewModel.fixAuditEncryption(cameraId) },
                        onFixVlan = { cameraId: String -> viewModel.fixAuditVlan(cameraId) },
                        modifier = Modifier.padding(innerPadding)
                    )

                    MainTab.PROFILE -> ProfileScreen(
                        uiState = uiState,
                        onToggleNightVision = { viewModel.toggleNightVision() },
                        onToggleVpn = { viewModel.toggleVpn() },
                        onToggleProxyServer = { viewModel.toggleProxyServer() },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
