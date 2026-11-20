package com.example.ui.screens

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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppInventorStep
import com.example.model.SecurityLesson
import com.example.ui.theme.CyberAmber
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.CyberGreen
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

val defensiveLessons = listOf(
    SecurityLesson(
        id = 1,
        moduleNumber = 1,
        title = "Defensive Guarding vs. Spying",
        subtitle = "The ethical boundary of authorized monitoring",
        category = "FOUNDATIONAL ETHICS",
        readTime = "3 min read",
        conceptOverview = "In cybersecurity, surveillance cameras are defensive perimeters designed to protect occupants and property against unauthorized intrusion. Spying, by contrast, is covert, unauthorized observation that violates privacy and criminal laws.",
        whyItMatters = "Building a CyberGuard app requires keeping the focus entirely on defense: alerting owners to breaches, securing communication channels, and never providing backdoors or unauthorized camera tapping.",
        defensiveRule = "DEFENSIVE RULE: Always obtain explicit authorization. Cameras must protect verified perimeters with visible status indicators, never secret backdoors.",
        appInventorSteps = listOf(
            AppInventorStep(
                stepNumber = 1,
                componentName = "Title_Label",
                actionType = "Add Component",
                details = "Drag a Label onto Screen1. Set Text to 'CCTV CyberGuard', Font Bold, Size 20.",
                securityPurpose = "Provides clear on-screen identification that defensive guard mode is active."
            ),
            AppInventorStep(
                stepNumber = 2,
                componentName = "Start_Btn",
                actionType = "Add Component & Rename",
                details = "Drag a Button below the title. Set Text to 'Start Guarding' and rename to Start_Btn.",
                securityPurpose = "Explicit user interaction required to arm the perimeter defense."
            ),
            AppInventorStep(
                stepNumber = 3,
                componentName = "Status_Label",
                actionType = "Set Property",
                details = "Set Text to 'Protecting your space from unauthorized eyes'. Alignment = Center.",
                securityPurpose = "Reinforces defensive mandate to the system operator."
            )
        )
    ),
    SecurityLesson(
        id = 2,
        moduleNumber = 2,
        title = "App Inventor Architecture for CyberGuard",
        subtitle = "Screen layout, visual blocks & event loops",
        category = "APP ARCHITECTURE",
        readTime = "4 min read",
        conceptOverview = "Mobile apps built in visual block environments like MIT App Inventor or Kodular use event-driven programming: 'When Start_Btn.Click', 'When Clock1.Timer', or 'When MotionDetected'.",
        whyItMatters = "Understanding the component palette (Designer View) and block connection (Blocks View) enables non-coders to assemble responsive defensive sensors without syntax errors.",
        defensiveRule = "DEFENSIVE RULE: Rename components logically (e.g., Start_Btn instead of Button1). Descriptive names prevent critical logic mistakes during security rule configuration.",
        appInventorSteps = listOf(
            AppInventorStep(
                stepNumber = 1,
                componentName = "VerticalArrangement",
                actionType = "Layout Organization",
                details = "Wrap screen contents in a VerticalArrangement with AlignHorizontal = Center.",
                securityPurpose = "Prevents components from scattering or overlapping on different device displays."
            ),
            AppInventorStep(
                stepNumber = 2,
                componentName = "Clock1",
                actionType = "Sensor Component",
                details = "Add a Clock sensor from Sensors palette. Set TimerInterval to 1000 ms.",
                securityPurpose = "Drives continuous defensive heartbeat checks across camera links."
            ),
            AppInventorStep(
                stepNumber = 3,
                componentName = "Notifier1",
                actionType = "UI Alert Dialog",
                details = "Add a Notifier from User Interface palette.",
                securityPurpose = "Pops up high-priority breach warnings when perimeter variance is triggered."
            )
        )
    ),
    SecurityLesson(
        id = 3,
        moduleNumber = 3,
        title = "Cryptographic Stream Tunnels (SRTP & TLS)",
        subtitle = "Preventing video eavesdropping and man-in-the-middle tampering",
        category = "NETWORK ENCRYPTION",
        readTime = "5 min read",
        conceptOverview = "Legacy IP cameras broadcast Real-Time Streaming Protocol (RTSP) in unencrypted plaintext on TCP port 554. Any device on the same local network or Wi-Fi can capture and reconstruct video frames with tools like Wireshark.",
        whyItMatters = "Secure Real-time Transport Protocol (SRTP) combined with TLS 1.3 encrypts each video packet using AES-256 and authenticates data integrity, preventing frame interception or spoofing.",
        defensiveRule = "DEFENSIVE RULE: Reject unencrypted RTSP streams on port 554. Always mandate RTSPS (RTSP over TLS) on port 322 or WebRTC with DTLS/SRTP.",
        appInventorSteps = listOf(
            AppInventorStep(
                stepNumber = 1,
                componentName = "Stream_Status_Label",
                actionType = "Add Component",
                details = "Set Text to 'ENCRYPTION: SRTP / TLS 1.3 (Cipher Active)'. TextColor = Green.",
                securityPurpose = "Informs operator that the video pipeline is cryptographically sealed."
            ),
            AppInventorStep(
                stepNumber = 2,
                componentName = "Cipher_Verifier",
                actionType = "Block Logic",
                details = "When Screen1.Initialize -> Check if stream URL starts with 'rtsps://'. If not, halt stream.",
                securityPurpose = "Enforces cryptographic minimums before displaying video pixels."
            )
        )
    ),
    SecurityLesson(
        id = 4,
        moduleNumber = 4,
        title = "Router Hardening: Disabling UPnP & VLAN Isolation",
        subtitle = "Neutralizing the #1 entry point for camera botnets",
        category = "PERIMETER HARDENING",
        readTime = "4 min read",
        conceptOverview = "Universal Plug and Play (UPnP) allows IP cameras to request automatic port forwarding on home routers. Search engines like Shodan index these open ports, making cameras accessible to the global internet.",
        whyItMatters = "Isolating cameras onto a separate Virtual LAN (VLAN) without WAN routing ensures that even if an attacker finds an unpatched firmware exploit, they cannot reach your laptops, phones, or server vaults.",
        defensiveRule = "DEFENSIVE RULE: Disable UPnP on your router. Place all surveillance devices in an isolated IoT VLAN and access feeds exclusively through a local VPN.",
        appInventorSteps = listOf(
            AppInventorStep(
                stepNumber = 1,
                componentName = "Audit_Warning_Card",
                actionType = "Add Layout",
                details = "Create a red warning banner showing 'UPnP Status: BLOCKED'.",
                securityPurpose = "Visually alerts if a camera attempts external port negotiation."
            ),
            AppInventorStep(
                stepNumber = 2,
                componentName = "VLAN_Indicator",
                actionType = "Set Property",
                details = "Display current network subnet: 'Subnet 10.0.30.0/24 (Surveillance VLAN Sandbox)'.",
                securityPurpose = "Validates proper network segmentation before arming alarms."
            )
        )
    ),
    SecurityLesson(
        id = 5,
        moduleNumber = 5,
        title = "Geo-Location Proximity & Automated Defense",
        subtitle = "Context-aware automated camera connection",
        category = "GEO-FENCING DEFENSE",
        readTime = "4 min read",
        conceptOverview = "Using device GPS and Google Play Services Fused Location Provider allows the CyberGuard app to calculate real-time proximity to registered camera zones using the Haversine spherical distance formula.",
        whyItMatters = "When the authorized user enters a specific camera zone (e.g. Front Entrance), the app automatically locks onto that zone's feed, saving bandwidth and prioritizing immediate local threats.",
        defensiveRule = "DEFENSIVE RULE: Geofence radiuses should match physical property perimeters (30-60 meters). Do not expose camera telemetry beyond authorized geographical boundaries.",
        appInventorSteps = listOf(
            AppInventorStep(
                stepNumber = 1,
                componentName = "LocationSensor1",
                actionType = "Sensors Palette",
                details = "Add LocationSensor. Set TimeInterval to 10000 ms, DistanceInterval to 5 meters.",
                securityPurpose = "Acquires device GPS coordinates for perimeter proximity calculations."
            ),
            AppInventorStep(
                stepNumber = 2,
                componentName = "Radar_Display",
                actionType = "Canvas & Drawing",
                details = "Add a Canvas component with green circular range rings representing 30m, 60m, 90m perimeters.",
                securityPurpose = "Provides tactical spatial awareness of active security zones."
            )
        )
    )
)

@Composable
fun LessonsScreen(
    onBack: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var expandedLessonId by remember { mutableStateOf<Int?>(1) }

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
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = CyberCyan
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "CYBERSECURITY CURRICULUM",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            color = CyberCyan
                        )
                        Text(
                            text = "Defensive CCTV Modules",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Bite-sized tactical lessons covering authorized CCTV protection, zero-trust network hardening, and no-code app building.",
                    fontSize = 13.sp,
                    color = TextSecondary
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        itemsIndexed(defensiveLessons, key = { _, lesson -> lesson.id }) { index, lesson ->
            val isExpanded = expandedLessonId == lesson.id

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("lesson_card_${lesson.id}"),
                colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (isExpanded) CyberCyan.copy(alpha = 0.5f) else DarkBorderSubtle
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    // Header row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                expandedLessonId = if (isExpanded) null else lesson.id
                            },
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    color = CyberCyan.copy(alpha = 0.15f),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = "MODULE ${lesson.moduleNumber}",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = CyberCyan,
                                        fontFamily = FontFamily.Monospace,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = lesson.readTime,
                                    fontSize = 11.sp,
                                    color = TextMuted
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = lesson.title,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = lesson.subtitle,
                                fontSize = 12.sp,
                                color = TextSecondary
                            )
                        }

                        IconButton(
                            onClick = {
                                expandedLessonId = if (isExpanded) null else lesson.id
                            }
                        ) {
                            Icon(
                                imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                contentDescription = if (isExpanded) "Collapse" else "Expand",
                                tint = CyberCyan
                            )
                        }
                    }

                    // Expanded body
                    AnimatedVisibility(visible = isExpanded) {
                        Column(modifier = Modifier.padding(top = 14.dp)) {
                            // Concept overview
                            Text(
                                text = "TACTICAL CONCEPT",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                color = CyberPurpleLight
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = lesson.conceptOverview,
                                fontSize = 13.sp,
                                color = TextPrimary,
                                lineHeight = 18.sp
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            // Why it matters
                            Text(
                                text = "WHY DEFENSE MATTERS",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                color = CyberAmber
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = lesson.whyItMatters,
                                fontSize = 13.sp,
                                color = TextSecondary,
                                lineHeight = 18.sp
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            // Defensive Rule Callout Banner
                            Surface(
                                color = DarkBackgroundObsidian,
                                shape = RoundedCornerShape(10.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, CyberGreen.copy(alpha = 0.4f))
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Shield,
                                        contentDescription = null,
                                        tint = CyberGreen,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = lesson.defensiveRule,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = CyberGreen,
                                        lineHeight = 16.sp
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            // MIT App Inventor Steps
                            Text(
                                text = "APP INVENTOR PRACTICAL BUILD STEPS",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                color = CyberCyan
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            lesson.appInventorSteps.forEach { step ->
                                Surface(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    color = DarkSurfaceCardElevated,
                                    shape = RoundedCornerShape(8.dp),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorderSubtle)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(10.dp),
                                        verticalAlignment = Alignment.Top
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(20.dp)
                                                .clip(CircleShape)
                                                .background(CyberCyan),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = "${step.stepNumber}",
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = DarkBackgroundObsidian
                                            )
                                        }

                                        Spacer(modifier = Modifier.width(10.dp))

                                        Column(modifier = Modifier.weight(1f)) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween
                                            ) {
                                                Text(
                                                    text = step.componentName,
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = TextPrimary
                                                )
                                                Surface(
                                                    color = CyberPurple.copy(alpha = 0.2f),
                                                    shape = RoundedCornerShape(4.dp)
                                                ) {
                                                    Text(
                                                        text = step.actionType,
                                                        fontSize = 9.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = CyberPurpleLight,
                                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                                    )
                                                }
                                            }

                                            Spacer(modifier = Modifier.height(3.dp))
                                            Text(
                                                text = step.details,
                                                fontSize = 12.sp,
                                                color = TextSecondary,
                                                lineHeight = 16.sp
                                            )

                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                text = "Defensive Goal: ${step.securityPurpose}",
                                                fontSize = 11.sp,
                                                color = CyberCyan,
                                                lineHeight = 15.sp
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(14.dp))
        }
    }
}
