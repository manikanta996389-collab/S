package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.Radar
import androidx.compose.material.icons.filled.Shield
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
import com.example.ui.theme.CyberAmber
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.CyberGreen
import com.example.ui.theme.CyberPurple
import com.example.ui.theme.CyberPurpleDark
import com.example.ui.theme.CyberPurpleLight
import com.example.ui.theme.DarkBackgroundObsidian
import com.example.ui.theme.DarkBorderSubtle
import com.example.ui.theme.DarkSurfaceCard
import com.example.ui.theme.DarkSurfaceCardElevated
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun MissionsScreen(
    onOpenTriage: () -> Unit,
    onOpenQuiz: () -> Unit,
    onOpenLessons: () -> Unit,
    onOpenAudit: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackgroundObsidian)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 96.dp)
    ) {
        item {
            Column {
                Text(
                    text = "MISSION BRIEFINGS",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    color = CyberCyan
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Choose Your Path",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Complete missions to level up and earn rewards",
                    fontSize = 13.sp,
                    color = TextSecondary
                )
                Spacer(modifier = Modifier.height(20.dp))
            }
        }

        // Mission 1: Triage Trainer
        item {
            MissionCard(
                tag = "TIER 1",
                xpReward = "+50 XP",
                title = "Triage Trainer",
                description = "Sort security alerts into Escalate or Dismiss. Train your SOC Analyst instincts.",
                icon = Icons.Default.Radar,
                iconColor = CyberPurpleLight,
                iconBg = CyberPurpleDark.copy(alpha = 0.4f),
                onClick = onOpenTriage,
                testTag = "mission_triage"
            )
            Spacer(modifier = Modifier.height(14.dp))
        }

        // Mission 2: Daily Quiz
        item {
            MissionCard(
                tag = "QUICK",
                xpReward = "+10-20 XP",
                title = "Daily Quiz",
                description = "Test your security knowledge with rapid-fire questions. Speed matters!",
                icon = Icons.Default.FlashOn,
                iconColor = CyberAmber,
                iconBg = CyberAmber.copy(alpha = 0.2f),
                onClick = onOpenQuiz,
                testTag = "mission_quiz"
            )
            Spacer(modifier = Modifier.height(14.dp))
        }

        // Mission 3: Micro-Learning
        item {
            MissionCard(
                tag = "5 MIN",
                xpReward = "+12 XP",
                title = "Micro-Learning",
                description = "Bite-sized lessons on CCTV security, layout boundaries, and defense fundamentals.",
                icon = Icons.Default.MenuBook,
                iconColor = CyberCyan,
                iconBg = CyberCyan.copy(alpha = 0.2f),
                onClick = onOpenLessons,
                testTag = "mission_lessons"
            )
            Spacer(modifier = Modifier.height(14.dp))
        }

        // Mission 4: Camera Hardening Audit
        item {
            MissionCard(
                tag = "TIER 2",
                xpReward = "+75 XP",
                title = "Camera Hardening Audit",
                description = "Inspect IP cameras, eliminate default passwords, disable UPnP, and patch CVEs.",
                icon = Icons.Default.Policy,
                iconColor = CyberGreen,
                iconBg = CyberGreen.copy(alpha = 0.2f),
                onClick = onOpenAudit,
                testTag = "mission_audit"
            )
        }
    }
}

@Composable
fun MissionCard(
    tag: String,
    xpReward: String,
    title: String,
    description: String,
    icon: ImageVector,
    iconColor: Color,
    iconBg: Color,
    onClick: () -> Unit,
    testTag: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag(testTag),
        colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
        border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorderSubtle),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = DarkSurfaceCardElevated,
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = "• $tag",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        color = TextSecondary,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }

                Surface(
                    color = CyberPurple.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = xpReward,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        color = CyberPurpleLight,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = iconBg,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.size(46.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = iconColor,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = description,
                        fontSize = 12.sp,
                        color = TextSecondary,
                        lineHeight = 16.sp
                    )
                }

                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = TextMuted,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
