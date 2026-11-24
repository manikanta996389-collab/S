package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoFixHigh
import androidx.compose.material.icons.filled.CenterFocusStrong
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.VideoClipStorageManager
import com.example.model.CameraZone
import com.example.model.PrivacyMask
import com.example.model.PrivacyMaskStyle
import com.example.ui.theme.CyberAmber
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.CyberGreen
import com.example.ui.theme.CyberNavyBorder
import com.example.ui.theme.CyberRed
import com.example.ui.theme.TextSecondary
import kotlinx.coroutines.delay
import java.io.File
import kotlin.math.roundToInt

@Composable
fun CameraMonitorView(
    cameraZone: CameraZone,
    isGuardingActive: Boolean,
    isNightVision: Boolean,
    simulatedBreach: Boolean,
    isDeviceCameraActive: Boolean = false,
    hasCameraPermission: Boolean = false,
    isFrontCamera: Boolean = false,
    privacyMasks: List<PrivacyMask> = emptyList(),
    isPrivacyMaskingEnabled: Boolean = true,
    privacyMaskStyle: PrivacyMaskStyle = PrivacyMaskStyle.BLACKOUT,
    isClarityFilterActive: Boolean = false,
    refocusPulseCount: Int = 0,
    isRecording: Boolean = false,
    recordingDurationMs: Long = 0L,
    recordingOutputFile: File? = null,
    onMaskClicked: (PrivacyMask) -> Unit = {},
    onRequestCameraPermission: () -> Unit = {},
    onToggleCameraFacing: () -> Unit = {},
    onTriggerRefocus: () -> Unit = {},
    onOpenBlurDiagnostic: () -> Unit = {},
    onRecordingStarted: () -> Unit = {},
    onRecordingProgress: (Long) -> Unit = {},
    onRecordingFinalized: (File, Long) -> Unit = { _, _ -> },
    onRecordingError: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "RadarSweep")
    val scanLineY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(3500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "ScanLineAnimation"
    )

    val blinkAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "BlinkAlpha"
    )

    // Animated focus ring state
    var focusRingOffset by remember { mutableStateOf<Offset?>(null) }
    var focusRingTrigger by remember { mutableStateOf(0) }
    val focusRingScale = remember { Animatable(1.6f) }
    val focusRingAlpha = remember { Animatable(1f) }

    LaunchedEffect(refocusPulseCount) {
        if (refocusPulseCount > 0) {
            focusRingTrigger++
        }
    }

    LaunchedEffect(focusRingTrigger) {
        if (focusRingTrigger > 0) {
            focusRingScale.snapTo(1.7f)
            focusRingAlpha.snapTo(1f)
            focusRingScale.animateTo(1.0f, tween(350, easing = LinearEasing))
            delay(500)
            focusRingAlpha.animateTo(0f, tween(300))
            focusRingOffset = null
        }
    }

    val frameBg = when {
        simulatedBreach -> Color(0xFF280B14)
        isNightVision -> Color(0xFF04190C) // Phosphor Night Vision
        else -> Color(0xFF0B1424) // Deep Cyber Blue
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(235.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(frameBg)
            .border(
                width = if (simulatedBreach || isRecording) 2.dp else 1.dp,
                color = when {
                    isRecording -> CyberRed
                    simulatedBreach -> CyberRed
                    isGuardingActive -> CyberCyan
                    else -> CyberNavyBorder
                },
                shape = RoundedCornerShape(16.dp)
            )
            .testTag("cctv_camera_monitor_frame")
    ) {
        if (isDeviceCameraActive && hasCameraPermission) {
            DeviceCameraPreviewView(
                isFrontCamera = isFrontCamera,
                refocusTrigger = refocusPulseCount,
                isRecordingRequested = isRecording,
                recordingOutputFile = recordingOutputFile,
                onFocusTriggered = { offset ->
                    focusRingOffset = offset
                    focusRingTrigger++
                },
                onRecordingStarted = onRecordingStarted,
                onRecordingProgress = onRecordingProgress,
                onRecordingFinalized = onRecordingFinalized,
                onRecordingError = onRecordingError,
                modifier = Modifier.fillMaxSize()
            )

            // Night Vision green phosphor color grading
            if (isNightVision) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0x3300E676))
                )
            }

            // Clarity / Edge-sharpening contrast enhancement overlay filter
            if (isClarityFilterActive) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0x1800E5FF))
                )
            }
        }

        // Privacy Masking Spatial Overlay (Masks sensitive areas from feed)
        if (isPrivacyMaskingEnabled && privacyMasks.isNotEmpty()) {
            PrivacyMaskOverlay(
                masks = privacyMasks,
                isEnabled = isPrivacyMaskingEnabled,
                style = privacyMaskStyle,
                onMaskClicked = onMaskClicked,
                modifier = Modifier.fillMaxSize()
            )
        }

        // High-Tech Canvas Graphics: Grid, Crosshairs, Scanline, Target Box
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            // 1. Subtle Camera Grid Lines
            val gridColor = if (isNightVision) Color(0x2200E676) else Color(0x1800E5FF)
            val stroke = Stroke(width = 1f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 6f), 0f))
            
            // Horizontal grid
            drawLine(gridColor, Offset(0f, h * 0.25f), Offset(w, h * 0.25f), strokeWidth = 1f)
            drawLine(gridColor, Offset(0f, h * 0.5f), Offset(w, h * 0.5f), strokeWidth = 1f)
            drawLine(gridColor, Offset(0f, h * 0.75f), Offset(w, h * 0.75f), strokeWidth = 1f)

            // Vertical grid
            drawLine(gridColor, Offset(w * 0.33f, 0f), Offset(w * 0.33f, h), strokeWidth = 1f)
            drawLine(gridColor, Offset(w * 0.66f, 0f), Offset(w * 0.66f, h), strokeWidth = 1f)

            // 2. Corner Reticle Brackets [   ]
            val bracketColor = if (simulatedBreach) CyberRed else if (isGuardingActive) CyberCyan else Color.Gray
            val bracketLen = 22.dp.toPx()
            val bracketStroke = 2.dp.toPx()
            val pad = 12.dp.toPx()

            // Top-Left
            drawLine(bracketColor, Offset(pad, pad), Offset(pad + bracketLen, pad), bracketStroke)
            drawLine(bracketColor, Offset(pad, pad), Offset(pad, pad + bracketLen), bracketStroke)

            // Top-Right
            drawLine(bracketColor, Offset(w - pad, pad), Offset(w - pad - bracketLen, pad), bracketStroke)
            drawLine(bracketColor, Offset(w - pad, pad), Offset(w - pad, pad + bracketLen), bracketStroke)

            // Bottom-Left
            drawLine(bracketColor, Offset(pad, h - pad), Offset(pad + bracketLen, h - pad), bracketStroke)
            drawLine(bracketColor, Offset(pad, h - pad), Offset(pad, h - pad - bracketLen), bracketStroke)

            // Bottom-Right
            drawLine(bracketColor, Offset(w - pad, h - pad), Offset(w - pad - bracketLen, h - pad), bracketStroke)
            drawLine(bracketColor, Offset(w - pad, h - pad), Offset(w - pad, h - pad - bracketLen), bracketStroke)

            // 3. Center Crosshair
            val cx = w / 2f
            val cy = h / 2f
            val chLen = 14.dp.toPx()
            drawLine(bracketColor.copy(alpha = 0.5f), Offset(cx - chLen, cy), Offset(cx + chLen, cy), 1.5f)
            drawLine(bracketColor.copy(alpha = 0.5f), Offset(cx, cy - chLen), Offset(cx, cy + chLen), 1.5f)
            drawCircle(bracketColor.copy(alpha = 0.35f), radius = 22.dp.toPx(), center = Offset(cx, cy), style = Stroke(1.5f))

            // 4. Moving Scan Line
            val currentScanY = h * scanLineY
            drawLine(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color.Transparent,
                        if (simulatedBreach) CyberRed.copy(alpha = 0.7f) else if (isNightVision) CyberGreen.copy(alpha = 0.7f) else CyberCyan.copy(alpha = 0.7f),
                        Color.Transparent
                    )
                ),
                start = Offset(0f, currentScanY),
                end = Offset(w, currentScanY),
                strokeWidth = 2.dp.toPx()
            )

            // 5. Motion / Detection Target Bounding Box
            if (simulatedBreach) {
                val boxX = w * 0.38f
                val boxY = h * 0.28f
                val boxW = w * 0.32f
                val boxH = h * 0.52f
                drawRect(
                    color = CyberRed.copy(alpha = blinkAlpha * 0.25f),
                    topLeft = Offset(boxX, boxY),
                    size = Size(boxW, boxH)
                )
                drawRect(
                    color = CyberRed,
                    topLeft = Offset(boxX, boxY),
                    size = Size(boxW, boxH),
                    style = Stroke(width = 2.dp.toPx())
                )
            } else if (isGuardingActive) {
                // Safe Zone Green Perimeter line
                drawRect(
                    color = CyberGreen.copy(alpha = 0.15f),
                    topLeft = Offset(w * 0.15f, h * 0.2f),
                    size = Size(w * 0.7f, h * 0.6f),
                    style = stroke
                )
            }
        }

        // Animated Focus Ring Reticle on Tap / Refocus
        val currentFocusOffset = focusRingOffset ?: Offset(180f, 110f)
        if (focusRingAlpha.value > 0.05f) {
            Box(
                modifier = Modifier
                    .offset {
                        IntOffset(
                            (currentFocusOffset.x - 30.dp.toPx() * focusRingScale.value).roundToInt(),
                            (currentFocusOffset.y - 30.dp.toPx() * focusRingScale.value).roundToInt()
                        )
                    }
                    .size((60.dp * focusRingScale.value))
                    .border(1.5.dp, CyberGreen.copy(alpha = focusRingAlpha.value), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "AF LOCK",
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    color = CyberGreen.copy(alpha = focusRingAlpha.value)
                )
            }
        }

        // Live HUD Top Bar: Streamlined layout with no overlapping elements
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left Column/Row: Status dot + LIVE tag + Camera Name
            Row(
                modifier = Modifier.weight(1f, fill = false),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(
                            when {
                                isRecording -> CyberRed.copy(alpha = blinkAlpha)
                                isGuardingActive -> CyberGreen.copy(alpha = blinkAlpha)
                                else -> Color.Gray
                            }
                        )
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = when {
                        isRecording -> "REC ${VideoClipStorageManager.formatDuration(recordingDurationMs)}"
                        isGuardingActive -> "LIVE"
                        else -> "STANDBY"
                    },
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    color = when {
                        isRecording -> CyberRed
                        isGuardingActive -> CyberGreen
                        else -> Color.LightGray
                    },
                    modifier = Modifier.testTag("hud_live_rec_indicator")
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (isDeviceCameraActive) (if (isFrontCamera) "Device (Front)" else "Device (Rear)") else cameraZone.name,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, fill = false)
                )

                if (isPrivacyMaskingEnabled && privacyMasks.any { it.isEnabled }) {
                    Spacer(modifier = Modifier.width(5.dp))
                    Surface(
                        color = CyberCyan.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(3.dp)
                    ) {
                        Text(
                            text = "MASK",
                            fontSize = 7.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            color = CyberCyan,
                            modifier = Modifier.padding(horizontal = 3.dp, vertical = 1.dp)
                        )
                    }
                }
            }

            // Right Row: Clarity Badge, Lens Switch, & Security Protocol Badge
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                if (isClarityFilterActive) {
                    Surface(
                        color = CyberCyan.copy(alpha = 0.25f),
                        shape = RoundedCornerShape(4.dp),
                        border = androidx.compose.foundation.BorderStroke(0.5.dp, CyberCyan)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoFixHigh,
                                contentDescription = "Clarity Boost",
                                tint = CyberCyan,
                                modifier = Modifier.size(9.dp)
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = "SHARP",
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                color = CyberCyan
                            )
                        }
                    }
                }

                if (isDeviceCameraActive && hasCameraPermission) {
                    IconButton(
                        onClick = onToggleCameraFacing,
                        modifier = Modifier
                            .size(24.dp)
                            .testTag("in_hud_switch_lens_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Cameraswitch,
                            contentDescription = "Switch Camera Lens",
                            tint = CyberCyan,
                            modifier = Modifier.size(15.dp)
                        )
                    }
                }

                Surface(
                    color = Color(0x77000000),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Encrypted Stream",
                            tint = CyberCyan,
                            modifier = Modifier.size(10.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = if (isDeviceCameraActive) "TLS 1.3" else "SRTP",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            color = CyberCyan
                        )
                    }
                }
            }
        }

        // Center Alert Tag if Breach / Motion Detected
        if (simulatedBreach) {
            Surface(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(horizontal = 16.dp),
                color = Color(0xDD990022),
                shape = RoundedCornerShape(8.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, CyberRed)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "Breach Alert",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "PERIMETER MOTION: UNVERIFIED ENTITY",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        } else if (!isGuardingActive && !(isDeviceCameraActive && !hasCameraPermission)) {
            Surface(
                modifier = Modifier.align(Alignment.Center),
                color = Color(0xAA000000),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "DEFENSIVE SHIELD PAUSED\nTap 'Start Guarding' to arm perimeter",
                    color = Color.LightGray,
                    fontSize = 11.sp,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                )
            }
        }

        // In-HUD Camera Permission Rationale & Request Card
        if (isDeviceCameraActive && !hasCameraPermission) {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp),
                color = Color(0xF5060C18),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, CyberCyan)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(CyberCyan.copy(alpha = 0.15f))
                            .border(1.5.dp, CyberCyan, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Videocam,
                            contentDescription = "Camera Permission Required",
                            tint = CyberCyan,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "DEVICE CAMERA ACCESS REQUIRED",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Camera access is needed to capture and monitor CCTV surveillance on this terminal.",
                        fontSize = 10.sp,
                        color = TextSecondary,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        lineHeight = 14.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(
                        onClick = onRequestCameraPermission,
                        modifier = Modifier.testTag("in_hud_grant_camera_btn"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CyberCyan,
                            contentColor = Color(0xFF00363D)
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
                            text = "Grant Camera Permission",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Live HUD Bottom Bar: Resolution, FPS, Refocus Tap Hint, Diagnostic Info
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(Color(0x99060B14))
                .padding(horizontal = 10.dp, vertical = 5.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = if (isDeviceCameraActive) "1080p • 60FPS" else "${cameraZone.resolution} • 30FPS",
                    fontSize = 9.sp,
                    fontFamily = FontFamily.Monospace,
                    color = Color(0xFF94A3B8)
                )

                if (isDeviceCameraActive && hasCameraPermission) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(
                        modifier = Modifier
                            .clip(RoundedCornerShape(3.dp))
                            .clickable { onOpenBlurDiagnostic() },
                        color = CyberAmber.copy(alpha = 0.15f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = "Blur Diagnostics",
                                tint = CyberAmber,
                                modifier = Modifier.size(9.dp)
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = "Blur Help",
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                color = CyberAmber
                            )
                        }
                    }
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                if (isNightVision) {
                    Text(
                        text = "IR NIGHT",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        color = CyberGreen
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                }
                Text(
                    text = "UTC 15:58 SEC",
                    fontSize = 9.sp,
                    fontFamily = FontFamily.Monospace,
                    color = Color.LightGray
                )
            }
        }
    }
}
