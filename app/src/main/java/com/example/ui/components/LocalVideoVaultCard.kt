package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.FiberManualRecord
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.VideoClipStorageManager
import com.example.model.ClipNamingPattern
import com.example.model.SavedVideoClip
import com.example.ui.theme.CyberAmber
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.CyberGreen
import com.example.ui.theme.CyberRed
import com.example.ui.theme.DarkBorderSubtle
import com.example.ui.theme.DarkSurfaceCard
import com.example.ui.theme.DarkSurfaceCardElevated
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

/**
 * Local Internal Storage Video Vault Card.
 * Lets users customize the filename prefix + timestamp naming convention,
 * start/stop local MP4 recording, inspect internal storage consumption,
 * and play or delete saved clips.
 */
@Composable
fun LocalVideoVaultCard(
    isRecording: Boolean,
    recordingDurationMs: Long,
    clipNamingPrefix: String,
    clipNamingPattern: ClipNamingPattern,
    cameraSourceTag: String,
    savedClips: List<SavedVideoClip>,
    totalStorageBytes: Long,
    recordingStatusMessage: String?,
    onPrefixChange: (String) -> Unit,
    onPatternSelect: (ClipNamingPattern) -> Unit,
    onToggleRecord: () -> Unit,
    onPlayClip: (SavedVideoClip) -> Unit,
    onDeleteClip: (SavedVideoClip) -> Unit,
    onClearAllClips: () -> Unit,
    modifier: Modifier = Modifier
) {
    val previewFileName = remember(clipNamingPrefix, clipNamingPattern, cameraSourceTag) {
        VideoClipStorageManager.buildClipFileName(
            customPrefix = clipNamingPrefix,
            cameraSourceTag = cameraSourceTag,
            pattern = clipNamingPattern
        )
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("local_video_vault_card"),
        colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
        border = BorderStroke(
            width = 1.dp,
            color = if (isRecording) CyberRed.copy(alpha = 0.8f) else CyberCyan.copy(alpha = 0.4f)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header
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
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isRecording) CyberRed.copy(alpha = 0.18f) else CyberCyan.copy(alpha = 0.14f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.VideoLibrary,
                            contentDescription = "Local Video Vault",
                            tint = if (isRecording) CyberRed else CyberCyan,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Local Video Clip Vault",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "${savedClips.size} CLIPS • ${VideoClipStorageManager.formatFileSize(totalStorageBytes)} INTERNAL",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            color = if (isRecording) CyberRed else CyberGreen
                        )
                    }
                }

                if (savedClips.isNotEmpty() && !isRecording) {
                    OutlinedButton(
                        onClick = onClearAllClips,
                        modifier = Modifier
                            .height(32.dp)
                            .testTag("clear_all_clips_btn"),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = CyberRed),
                        border = BorderStroke(1.dp, CyberRed.copy(alpha = 0.45f)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.DeleteSweep,
                            contentDescription = "Clear All Clips",
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Clear", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Naming Convention Configuration Panel
            Surface(
                color = DarkSurfaceCardElevated,
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, DarkBorderSubtle),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Save,
                            contentDescription = null,
                            tint = CyberCyan,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "CUSTOM FILENAME & TIMESTAMP CONVENTION",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            color = CyberCyan
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = clipNamingPrefix,
                        onValueChange = { input ->
                            if (input.length <= 20) {
                                onPrefixChange(input)
                            }
                        },
                        label = { Text("Custom Prefix (e.g., CCTV, GATE, VAULT)", fontSize = 11.sp) },
                        singleLine = true,
                        enabled = !isRecording,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("clip_naming_prefix_input"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CyberCyan,
                            unfocusedBorderColor = DarkBorderSubtle,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            focusedLabelColor = CyberCyan,
                            unfocusedLabelColor = TextMuted
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(ClipNamingPattern.entries) { pattern ->
                            val selected = pattern == clipNamingPattern
                            FilterChip(
                                selected = selected,
                                onClick = { if (!isRecording) onPatternSelect(pattern) },
                                enabled = !isRecording,
                                label = {
                                    Text(
                                        text = pattern.displayName,
                                        fontSize = 10.sp,
                                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = CyberCyan.copy(alpha = 0.2f),
                                    selectedLabelColor = CyberCyan,
                                    containerColor = DarkSurfaceCard,
                                    labelColor = TextSecondary
                                ),
                                border = FilterChipDefaults.filterChipBorder(
                                    enabled = !isRecording,
                                    selected = selected,
                                    borderColor = DarkBorderSubtle,
                                    selectedBorderColor = CyberCyan
                                ),
                                modifier = Modifier.testTag("naming_pattern_${pattern.name.lowercase()}")
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Live Filename Preview
                    Surface(
                        color = Color(0xFF070E1A),
                        shape = RoundedCornerShape(6.dp),
                        border = BorderStroke(1.dp, CyberCyan.copy(alpha = 0.25f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            Text(
                                text = "NEXT CLIP OUTPUT (INTERNAL STORAGE):",
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                color = TextMuted
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = previewFileName,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                color = CyberGreen,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.testTag("clip_filename_preview")
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Primary Record / Stop Action Button inside Vault Card
            Button(
                onClick = onToggleRecord,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("vault_record_clip_btn"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isRecording) CyberRed else CyberCyan,
                    contentColor = if (isRecording) Color.White else Color(0xFF00363D)
                ),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(
                    imageVector = if (isRecording) Icons.Default.Stop else Icons.Default.FiberManualRecord,
                    contentDescription = if (isRecording) "Stop Recording" else "Start Recording",
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isRecording) {
                        "Stop & Save Clip (${VideoClipStorageManager.formatDuration(recordingDurationMs)})"
                    } else {
                        "Record Clip to Internal Storage"
                    },
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            if (recordingStatusMessage != null) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = recordingStatusMessage,
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace,
                    color = if (recordingStatusMessage.startsWith("Error", ignoreCase = true)) CyberAmber else CyberGreen,
                    modifier = Modifier.testTag("recording_status_message")
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Saved Clips List
            if (savedClips.isEmpty()) {
                Surface(
                    color = DarkSurfaceCardElevated.copy(alpha = 0.6f),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, DarkBorderSubtle),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Folder,
                            contentDescription = null,
                            tint = TextMuted,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "No saved clips in internal storage yet",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextSecondary
                            )
                            Text(
                                text = "Tap 'Record Clip' to capture MP4 footage into private app storage (/cctv_clips).",
                                fontSize = 10.sp,
                                color = TextMuted,
                                lineHeight = 14.sp
                            )
                        }
                    }
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    savedClips.forEach { clip ->
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .clickable { onPlayClip(clip) }
                                .testTag("saved_clip_row_${clip.id}"),
                            color = DarkSurfaceCardElevated,
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(1.dp, DarkBorderSubtle)
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(CircleShape)
                                        .background(CyberCyan.copy(alpha = 0.16f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.PlayArrow,
                                        contentDescription = "Play ${clip.fileName}",
                                        tint = CyberCyan,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = clip.fileName,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.Monospace,
                                        color = TextPrimary,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "${VideoClipStorageManager.formatDuration(clip.durationMs)} • ${VideoClipStorageManager.formatFileSize(clip.sizeBytes)} • ${VideoClipStorageManager.formatTimestampDisplay(clip.createdAtMillis)}",
                                        fontSize = 10.sp,
                                        color = TextSecondary
                                    )
                                }
                                IconButton(
                                    onClick = { onDeleteClip(clip) },
                                    modifier = Modifier
                                        .size(48.dp)
                                        .testTag("delete_clip_btn_${clip.id}")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Delete ${clip.fileName}",
                                        tint = CyberRed.copy(alpha = 0.85f),
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
