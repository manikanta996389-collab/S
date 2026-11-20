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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.QuizQuestion
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
import com.example.viewmodel.CyberGuardUiState

val cyberQuestions = listOf(
    QuizQuestion(
        id = 1,
        question = "Why is keeping factory default credentials (e.g., admin/12345) on an IP camera an extreme vulnerability?",
        options = listOf(
            "It slows down video stream encoding",
            "Automated search engines and botnets routinely scan and compromise default credentials in seconds",
            "It turns off the camera's night vision infrared sensors",
            "It disables local Wi-Fi connectivity"
        ),
        correctIndex = 1,
        explanation = "Botnets and automated scanners constantly probe port 554 and 80 for standard factory passphrases. Changing to a unique, high-entropy password is rule #1."
    ),
    QuizQuestion(
        id = 2,
        question = "What critical risk does Universal Plug and Play (UPnP) introduce on surveillance routers?",
        options = listOf(
            "It degrades video frame rate to 15 FPS",
            "It automatically opens public router ports without user consent, exposing cameras directly to the Internet",
            "It invalidates cryptographic SSL certificates",
            "It forces cameras to reboot every 24 hours"
        ),
        correctIndex = 1,
        explanation = "UPnP allows IoT devices to forward external WAN ports automatically, making private cameras reachable from any scanner on the public internet without firewall protection."
    ),
    QuizQuestion(
        id = 3,
        question = "Why must surveillance systems employ SRTP / TLS encryption instead of legacy cleartext RTSP?",
        options = listOf(
            "To prevent adversaries on the local network from eavesdropping on or tampering with video frames",
            "To increase camera optical zoom range",
            "To enable cloud recording without internet access",
            "To avoid needing a power over ethernet switch"
        ),
        correctIndex = 0,
        explanation = "Cleartext RTSP transmits surveillance feeds unencrypted across network lines. Anyone on the local network could sniff or intercept private frames without TLS/SRTP."
    ),
    QuizQuestion(
        id = 4,
        question = "What is the primary defensive purpose of isolating security cameras on a dedicated VLAN?",
        options = listOf(
            "It makes video clips take up less storage on the NVR",
            "If a camera is compromised, the attacker cannot pivot to sensitive workstations or servers on the primary network",
            "It triples the Wi-Fi transmission speed",
            "It allows guest phones to access camera admin panels"
        ),
        correctIndex = 1,
        explanation = "Network segmentation (VLAN isolation) ensures that even if an IoT firmware vulnerability is exploited, the intruder remains trapped in a sandbox and cannot breach the corporate LAN."
    ),
    QuizQuestion(
        id = 5,
        question = "In ethical cybersecurity, what distinguishes authorized defensive guarding from illicit surveillance?",
        options = listOf(
            "Defensive guarding focuses on protecting property and authorized occupants under clear legal consent without unauthorized interception",
            "Defensive guarding only operates during daytime",
            "Defensive guarding does not require passwords",
            "Defensive guarding allows accessing neighboring cameras without permission"
        ),
        correctIndex = 0,
        explanation = "Defensive guarding is built on authorization, consent, privacy compliance, and hardening systems against intrusions, strictly rejecting unauthorized access or spying."
    )
)

@Composable
fun QuizScreen(
    uiState: CyberGuardUiState,
    onAnswer: (Int, Int, Boolean) -> Unit,
    onBack: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val totalCount = cyberQuestions.size
    val answeredCount = uiState.answeredQuestions.size

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
                            text = "KNOWLEDGE DEFENSE EVALUATION",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            color = CyberAmber
                        )
                        Text(
                            text = "Daily Security Quiz",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Test your grasp of IP camera security, zero-trust network isolation, and defensive triage.",
                    fontSize = 13.sp,
                    color = TextSecondary
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        // Score Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("quiz_score_banner"),
                colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
                border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorderSubtle),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "CURRENT SCORE",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            color = CyberCyan
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = "${uiState.quizScore}",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = CyberGreen
                            )
                            Text(
                                text = " / $totalCount Correct",
                                fontSize = 14.sp,
                                color = TextSecondary,
                                modifier = Modifier.padding(bottom = 4.dp, start = 4.dp)
                            )
                        }
                    }

                    Surface(
                        color = CyberAmber.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, CyberAmber)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.FlashOn,
                                contentDescription = null,
                                tint = CyberAmber,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "+${uiState.quizScore * 20} XP",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = CyberAmber,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(18.dp))
        }

        // Questions List
        itemsIndexed(cyberQuestions, key = { _, q -> q.id }) { index, question ->
            val userAnswer = uiState.answeredQuestions[question.id]
            val isAnswered = userAnswer != null

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("quiz_question_${question.id}"),
                colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (isAnswered && userAnswer == question.correctIndex) CyberGreen.copy(alpha = 0.4f)
                    else if (isAnswered) CyberRed.copy(alpha = 0.4f)
                    else DarkBorderSubtle
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = CyberPurple.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = "QUESTION ${index + 1} OF $totalCount",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = CyberPurpleLight,
                                fontFamily = FontFamily.Monospace,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }

                        if (isAnswered) {
                            val isCorrect = userAnswer == question.correctIndex
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = if (isCorrect) Icons.Default.CheckCircle else Icons.Default.Close,
                                    contentDescription = null,
                                    tint = if (isCorrect) CyberGreen else CyberRed,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (isCorrect) "+20 XP" else "Incorrect",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isCorrect) CyberGreen else CyberRed
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = question.question,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary,
                        lineHeight = 20.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Options
                    question.options.forEachIndexed { optIdx, optionText ->
                        val isSelected = userAnswer == optIdx
                        val isCorrectOption = optIdx == question.correctIndex

                        val bgColor = when {
                            !isAnswered -> DarkSurfaceCardElevated
                            isSelected && isCorrectOption -> CyberGreen.copy(alpha = 0.2f)
                            isSelected && !isCorrectOption -> CyberRed.copy(alpha = 0.2f)
                            isAnswered && isCorrectOption -> CyberGreen.copy(alpha = 0.1f)
                            else -> DarkSurfaceCardElevated
                        }

                        val borderColor = when {
                            !isAnswered && isSelected -> CyberCyan
                            !isAnswered -> DarkBorderSubtle
                            isSelected && isCorrectOption -> CyberGreen
                            isSelected && !isCorrectOption -> CyberRed
                            isAnswered && isCorrectOption -> CyberGreen.copy(alpha = 0.5f)
                            else -> DarkBorderSubtle
                        }

                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable(enabled = !isAnswered) {
                                    onAnswer(question.id, optIdx, optIdx == question.correctIndex)
                                },
                            color = bgColor,
                            border = androidx.compose.foundation.BorderStroke(1.dp, borderColor),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clip(CircleShape)
                                        .background(
                                            if (isSelected && isCorrectOption) CyberGreen
                                            else if (isSelected) CyberRed
                                            else DarkBorderSubtle
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = ('A'.code + optIdx).toChar().toString(),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) DarkBackgroundObsidian else TextSecondary
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = optionText,
                                    fontSize = 13.sp,
                                    color = if (isSelected) TextPrimary else TextSecondary,
                                    lineHeight = 17.sp,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }

                    // Explanation
                    if (isAnswered) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Surface(
                            color = DarkBackgroundObsidian,
                            shape = RoundedCornerShape(8.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorderSubtle)
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Psychology,
                                    contentDescription = null,
                                    tint = CyberCyan,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = "CYBERGUARD BRIEFING",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.Monospace,
                                        color = CyberCyan
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = question.explanation,
                                        fontSize = 12.sp,
                                        color = TextSecondary,
                                        lineHeight = 16.sp
                                    )
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
