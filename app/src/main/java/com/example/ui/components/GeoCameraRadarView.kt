package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GpsFixed
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CameraZone
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.CyberGreen
import com.example.ui.theme.CyberNavyBorder
import com.example.ui.theme.CyberRed
import com.example.ui.theme.DarkBackgroundObsidian
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun GeoCameraRadarView(
    cameras: List<CameraZone>,
    activeZoneIndex: Int,
    isAutoConnectActive: Boolean,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "RadarBeam")
    val sweepAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "RadarSweepAnimation"
    )

    val pulseRadius by infiniteTransition.animateFloat(
        initialValue = 6f,
        targetValue = 18f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "UserPulseAnimation"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF070E1A))
            .border(1.dp, CyberNavyBorder, RoundedCornerShape(16.dp))
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val cx = size.width / 2f
            val cy = size.height / 2f
            val maxR = minOf(size.width, size.height) * 0.42f

            val gridColor = Color(0x2200E5FF)
            val dashedStroke = Stroke(1.5f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 8f), 0f))

            // 1. Concentric Range Rings (25m, 50m, 100m)
            drawCircle(gridColor, radius = maxR * 0.33f, center = Offset(cx, cy), style = dashedStroke)
            drawCircle(gridColor, radius = maxR * 0.66f, center = Offset(cx, cy), style = dashedStroke)
            drawCircle(gridColor, radius = maxR, center = Offset(cx, cy), style = Stroke(1.5f))

            // Crosshair lines
            drawLine(gridColor, Offset(cx - maxR, cy), Offset(cx + maxR, cy), 1f)
            drawLine(gridColor, Offset(cx, cy - maxR), Offset(cx, cy + maxR), 1f)

            // 2. Rotating Radar Sweep Beam
            val rad = Math.toRadians(sweepAngle.toDouble())
            val beamEnd = Offset(
                cx + maxR * cos(rad).toFloat(),
                cy + maxR * sin(rad).toFloat()
            )
            drawLine(
                brush = Brush.radialGradient(
                    colors = listOf(CyberGreen.copy(alpha = 0.8f), Color.Transparent),
                    center = Offset(cx, cy),
                    radius = maxR
                ),
                start = Offset(cx, cy),
                end = beamEnd,
                strokeWidth = 2.5.dp.toPx()
            )

            // 3. User Central Position (Beacon)
            drawCircle(
                color = CyberCyan.copy(alpha = 0.3f),
                radius = pulseRadius.dp.toPx(),
                center = Offset(cx, cy)
            )
            drawCircle(
                color = CyberCyan,
                radius = 5.dp.toPx(),
                center = Offset(cx, cy)
            )

            // 4. Camera Positions relative to user center
            val offsets = listOf(
                Offset(cx - maxR * 0.45f, cy - maxR * 0.35f), // Zone 1
                Offset(cx + maxR * 0.55f, cy - maxR * 0.20f), // Zone 2
                Offset(cx + maxR * 0.15f, cy + maxR * 0.60f)  // Zone 3
            )

            cameras.forEachIndexed { idx, cam ->
                val camPos = offsets.getOrElse(idx) { Offset(cx, cy) }
                val isSelected = idx == activeZoneIndex
                val pinColor = if (isSelected) CyberGreen else CyberCyan.copy(alpha = 0.6f)

                // Dotted geofence circle around camera
                drawCircle(
                    color = pinColor.copy(alpha = 0.25f),
                    radius = 24.dp.toPx(),
                    center = camPos,
                    style = dashedStroke
                )

                // Active connection beam from user to camera
                if (isSelected && isAutoConnectActive) {
                    drawLine(
                        brush = Brush.linearGradient(
                            listOf(CyberCyan, CyberGreen)
                        ),
                        start = Offset(cx, cy),
                        end = camPos,
                        strokeWidth = 2.dp.toPx(),
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 6f), 0f)
                    )
                }

                // Camera blip
                drawCircle(
                    color = pinColor,
                    radius = if (isSelected) 7.dp.toPx() else 5.dp.toPx(),
                    center = camPos
                )
                if (isSelected) {
                    drawCircle(
                        color = Color.White,
                        radius = 2.dp.toPx(),
                        center = camPos
                    )
                }
            }
        }

        // Top Status Overlay
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.GpsFixed,
                    contentDescription = null,
                    tint = CyberCyan,
                    modifier = Modifier.size(13.dp)
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = "GEO-FENCE RADAR",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    color = CyberCyan
                )
            }

            Surface(
                color = if (isAutoConnectActive) CyberGreen.copy(alpha = 0.15f) else Color.DarkGray,
                shape = RoundedCornerShape(6.dp)
            ) {
                Text(
                    text = if (isAutoConnectActive) "AUTO-CONNECT: ARMED" else "AUTO-CONNECT: OFF",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isAutoConnectActive) CyberGreen else TextMuted,
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
        }

        // Bottom Distance / Lock Overlay
        val activeCam = cameras.getOrNull(activeZoneIndex)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(Color(0xCC070E1A))
                .padding(horizontal = 12.dp, vertical = 5.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Locked: ${activeCam?.name ?: "Searching"}",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Text(
                text = "${activeCam?.distanceMeters?.toInt() ?: 0}m distance • Within Geofence",
                fontSize = 10.sp,
                fontFamily = FontFamily.Monospace,
                color = CyberGreen
            )
        }
    }
}
