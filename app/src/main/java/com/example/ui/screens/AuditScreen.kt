package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.Router
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AuditCamera
import com.example.ui.components.CircularScoreGauge
import com.example.ui.theme.CyberAmber
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.CyberGreen
import com.example.ui.theme.CyberNavyBorder
import com.example.ui.theme.CyberPurple
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
fun AuditScreen(
    uiState: CyberGuardUiState,
    onFixPassword: (String) -> Unit,
    onFixUPnP: (String) -> Unit,
    onFixFirmware: (String) -> Unit,
    onFixEncryption: (String) -> Unit,
    onFixVlan: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val overallScore = uiState.overallScore()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackgroundObsidian)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 96.dp)
    ) {
        item {
            Column {
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
                                .background(CyberCyan)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "ZERO-TRUST POSTURE",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            color = CyberCyan
                        )
                    }
                    Text(
                        text = "AUDIT v3.4",
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        color = TextMuted
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Security Audit",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Inspect and remediate camera firmware, default credentials, and perimeter encryption flaws",
                    fontSize = 13.sp,
                    color = TextSecondary
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        // Overall Posture Gauge Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("audit_score_card"),
                colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
                border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorderSubtle),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "SYSTEM RESILIENCE",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            color = CyberGreen
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (overallScore >= 85) "Hardened Perimeter" else if (overallScore >= 60) "Action Required" else "Vulnerabilities Detected",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "${uiState.auditCameras.size} Authorized IP devices registered. Zero-trust compliance evaluated across 5 key defensive vectors.",
                            fontSize = 12.sp,
                            color = TextSecondary,
                            lineHeight = 16.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    CircularScoreGauge(
                        score = overallScore,
                        maxScore = 100,
                        modifier = Modifier.size(90.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }

        item {
            Text(
                text = "INSPECTED CAMERAS (${uiState.auditCameras.size})",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                color = TextSecondary
            )
            Spacer(modifier = Modifier.height(10.dp))
        }

        items(uiState.auditCameras, key = { it.id }) { camera ->
            CameraAuditCard(
                camera = camera,
                onFixPassword = { onFixPassword(camera.id) },
                onFixUPnP = { onFixUPnP(camera.id) },
                onFixFirmware = { onFixFirmware(camera.id) },
                onFixEncryption = { onFixEncryption(camera.id) },
                onFixVlan = { onFixVlan(camera.id) }
            )
            Spacer(modifier = Modifier.height(14.dp))
        }
    }
}

@Composable
private fun CameraAuditCard(
    camera: AuditCamera,
    onFixPassword: () -> Unit,
    onFixUPnP: () -> Unit,
    onFixFirmware: () -> Unit,
    onFixEncryption: () -> Unit,
    onFixVlan: () -> Unit
) {
    val score = camera.calculateScore()
    val isSecure = score >= 90

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("audit_camera_${camera.id}"),
        colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isSecure) CyberGreen.copy(alpha = 0.4f) else DarkBorderSubtle
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header: Name, IP/Port, Grade
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = camera.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${camera.ipAddress}:${camera.port}",
                        fontSize = 12.sp,
                        fontFamily = FontFamily.Monospace,
                        color = TextSecondary
                    )
                }

                Surface(
                    color = if (score >= 80) CyberGreen.copy(alpha = 0.15f) else if (score >= 60) CyberAmber.copy(alpha = 0.15f) else CyberRed.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(8.dp),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (score >= 80) CyberGreen else if (score >= 60) CyberAmber else CyberRed
                    )
                ) {
                    Text(
                        text = "GRADE ${camera.grade()} ($score/100)",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        color = if (score >= 80) CyberGreen else if (score >= 60) CyberAmber else CyberRed,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            if (camera.cveIdentifier != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    color = CyberRed.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(6.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CyberRed.copy(alpha = 0.5f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            tint = CyberRed,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = camera.cveIdentifier,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = CyberRed,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 5 Vulnerability Rows
            AuditVulnerabilityItem(
                title = "Vendor Default Passphrase",
                isVulnerable = camera.hasDefaultPassword,
                vulnerableLabel = "CRITICAL: Default admin/admin credentials",
                securedLabel = "SECURED: Strong Salted Passphrase",
                actionButtonText = "Enforce Strong Passphrase",
                icon = Icons.Default.Lock,
                onFix = onFixPassword
            )

            Spacer(modifier = Modifier.height(8.dp))

            AuditVulnerabilityItem(
                title = "UPnP Port Forwarding",
                isVulnerable = camera.isUPnPExposed,
                vulnerableLabel = "HIGH: UPnP exposed camera to public internet",
                securedLabel = "SECURED: UPnP Disabled (Local Only)",
                actionButtonText = "Disable UPnP / Close WAN",
                icon = Icons.Default.Router,
                onFix = onFixUPnP
            )

            Spacer(modifier = Modifier.height(8.dp))

            AuditVulnerabilityItem(
                title = "Firmware Security Patch",
                isVulnerable = camera.isFirmwareOutdated,
                vulnerableLabel = "HIGH: Outdated firmware with known exploits",
                securedLabel = "SECURED: Patched to latest secure release",
                actionButtonText = "Apply Security Patch",
                icon = Icons.Default.Build,
                onFix = onFixFirmware
            )

            Spacer(modifier = Modifier.height(8.dp))

            AuditVulnerabilityItem(
                title = "Stream Cryptographic Tunnel",
                isVulnerable = camera.isEncryptionMissing,
                vulnerableLabel = "MEDIUM: Cleartext RTSP 554 stream",
                securedLabel = "SECURED: SRTP / TLS 1.3 Active",
                actionButtonText = "Enforce TLS 1.3 / SRTP",
                icon = Icons.Default.Security,
                onFix = onFixEncryption
            )

            Spacer(modifier = Modifier.height(8.dp))

            AuditVulnerabilityItem(
                title = "Network Subnet Isolation (VLAN)",
                isVulnerable = camera.isVlanMissing,
                vulnerableLabel = "MEDIUM: Shared flat LAN with office devices",
                securedLabel = "SECURED: Segregated on Surveillance VLAN",
                actionButtonText = "Isolate to IoT VLAN",
                icon = Icons.Default.Wifi,
                onFix = onFixVlan
            )
        }
    }
}

@Composable
private fun AuditVulnerabilityItem(
    title: String,
    isVulnerable: Boolean,
    vulnerableLabel: String,
    securedLabel: String,
    actionButtonText: String,
    icon: ImageVector,
    onFix: () -> Unit
) {
    Surface(
        color = if (isVulnerable) CyberRed.copy(alpha = 0.08f) else DarkSurfaceCardElevated,
        shape = RoundedCornerShape(10.dp),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isVulnerable) CyberRed.copy(alpha = 0.35f) else DarkBorderSubtle
        )
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (isVulnerable) icon else Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = if (isVulnerable) CyberRed else CyberGreen,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = title,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = if (isVulnerable) vulnerableLabel else securedLabel,
                            fontSize = 11.sp,
                            color = if (isVulnerable) CyberAmber else CyberGreen
                        )
                    }
                }
            }

            if (isVulnerable) {
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = onFix,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CyberCyan,
                        contentColor = DarkBackgroundObsidian
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(34.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Shield,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = actionButtonText,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
