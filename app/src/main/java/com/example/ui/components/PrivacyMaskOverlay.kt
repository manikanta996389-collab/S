package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.PrivacyMask
import com.example.model.PrivacyMaskStyle
import com.example.ui.theme.CyberAmber
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.CyberGreen

/**
 * Enterprise CCTV Privacy Masking Overlay Component.
 * Obscures sensitive spatial zones (keypads, computer screens, windows, personal workspaces)
 * to maintain strict user privacy while retaining perimeter surveillance.
 */
@Composable
fun PrivacyMaskOverlay(
    masks: List<PrivacyMask>,
    isEnabled: Boolean,
    style: PrivacyMaskStyle,
    onMaskClicked: (PrivacyMask) -> Unit = {},
    modifier: Modifier = Modifier
) {
    if (!isEnabled || masks.isEmpty()) return

    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val totalWidth = maxWidth
        val totalHeight = maxHeight

        masks.filter { it.isEnabled }.forEach { mask ->
            val maskLeft = totalWidth * mask.leftRatio
            val maskTop = totalHeight * mask.topRatio
            val maskWidth = totalWidth * mask.widthRatio
            val maskHeight = totalHeight * mask.heightRatio

            Box(
                modifier = Modifier
                    .offset(x = maskLeft, y = maskTop)
                    .size(width = maskWidth, height = maskHeight)
                    .clip(RoundedCornerShape(6.dp))
                    .clickable { onMaskClicked(mask) }
            ) {
                when (style) {
                    PrivacyMaskStyle.BLACKOUT -> {
                        // Solid zero-trust opaque blackout
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color(0xFF030712))
                                .border(1.dp, CyberCyan.copy(alpha = 0.4f), RoundedCornerShape(6.dp))
                        ) {
                            // High-tech corner bracket reticles
                            Canvas(modifier = Modifier.fillMaxSize()) {
                                val w = size.width
                                val h = size.height
                                val strokeW = 2.dp.toPx()
                                val bracketSize = 10.dp.toPx()
                                val cyan = CyberCyan.copy(alpha = 0.7f)

                                // Top-Left
                                drawLine(cyan, Offset(0f, 0f), Offset(bracketSize, 0f), strokeW)
                                drawLine(cyan, Offset(0f, 0f), Offset(0f, bracketSize), strokeW)

                                // Top-Right
                                drawLine(cyan, Offset(w, 0f), Offset(w - bracketSize, 0f), strokeW)
                                drawLine(cyan, Offset(w, 0f), Offset(w, bracketSize), strokeW)

                                // Bottom-Left
                                drawLine(cyan, Offset(0f, h), Offset(bracketSize, h), strokeW)
                                drawLine(cyan, Offset(0f, h), Offset(0f, h - bracketSize), strokeW)

                                // Bottom-Right
                                drawLine(cyan, Offset(w, h), Offset(w - bracketSize, h), strokeW)
                                drawLine(cyan, Offset(w, h), Offset(w, h - bracketSize), strokeW)
                            }

                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(4.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Lock,
                                    contentDescription = "Masked Privacy Area",
                                    tint = CyberCyan,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = mask.label.uppercase(),
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace,
                                    color = Color.White,
                                    textAlign = TextAlign.Center
                                )
                                Text(
                                    text = "PRIVACY MASKED",
                                    fontSize = 7.sp,
                                    fontFamily = FontFamily.Monospace,
                                    color = CyberCyan
                                )
                            }
                        }
                    }

                    PrivacyMaskStyle.REDACTION_STRIPES -> {
                        // Tactical diagonal warning / redaction pattern
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val w = size.width
                            val h = size.height

                            // Dark base
                            drawRect(Color(0xFF0F172A))

                            // Draw diagonal hazard stripes
                            val stripeWidth = 14.dp.toPx()
                            var x = -h
                            while (x < w + h) {
                                drawLine(
                                    color = Color(0xFFD97706).copy(alpha = 0.35f),
                                    start = Offset(x, 0f),
                                    end = Offset(x + h, h),
                                    strokeWidth = stripeWidth
                                )
                                x += stripeWidth * 2.2f
                            }

                            // Outer caution border
                            drawRect(
                                color = CyberAmber.copy(alpha = 0.8f),
                                style = Stroke(width = 1.5.dp.toPx())
                            )
                        }

                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(4.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center
                        ) {
                            Surface(
                                color = Color(0xCC000000),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.VisibilityOff,
                                        contentDescription = null,
                                        tint = CyberAmber,
                                        modifier = Modifier.size(10.dp)
                                    )
                                    Spacer(modifier = Modifier.width(3.dp))
                                    Text(
                                        text = "RESTRICTED",
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        fontFamily = FontFamily.Monospace,
                                        color = CyberAmber
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = mask.label,
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    PrivacyMaskStyle.PIXEL_MOSAIC -> {
                        // High-density pixel mosaic simulation
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val w = size.width
                            val h = size.height
                            val blockSize = 12.dp.toPx()

                            val cols = (w / blockSize).toInt() + 1
                            val rows = (h / blockSize).toInt() + 1

                            val palette = listOf(
                                Color(0xFF0F172A),
                                Color(0xFF1E293B),
                                Color(0xFF334155),
                                Color(0xFF020617),
                                Color(0xFF111827)
                            )

                            for (r in 0 until rows) {
                                for (c in 0 until cols) {
                                    val colorIndex = (r * 7 + c * 13) % palette.size
                                    drawRect(
                                        color = palette[colorIndex],
                                        topLeft = Offset(c * blockSize, r * blockSize),
                                        size = Size(blockSize, blockSize)
                                    )
                                }
                            }

                            // Subtle pixel grid overlay
                            drawRect(
                                color = CyberGreen.copy(alpha = 0.5f),
                                style = Stroke(width = 1.dp.toPx())
                            )
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Surface(
                                color = Color(0xDD000000),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = "MOSAIC BLUR: ${mask.label.uppercase()}",
                                    fontSize = 7.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace,
                                    color = CyberGreen,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
