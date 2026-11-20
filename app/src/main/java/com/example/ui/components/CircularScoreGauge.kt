package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.CyberGreen
import com.example.ui.theme.CyberRed
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun CircularScoreGauge(
    score: Int,
    maxScore: Int = 100,
    subtitle: String = "Perimeter Scanned Just Now",
    size: Dp = 200.dp,
    modifier: Modifier = Modifier
) {
    val animatedScore by animateFloatAsState(
        targetValue = score.toFloat() / maxScore.toFloat(),
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
        label = "GaugeScoreAnimation"
    )

    val gaugeColor = when {
        score >= 90 -> CyberGreen
        score >= 70 -> CyberCyan
        else -> CyberRed
    }

    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(size)) {
            val strokeWidth = 12.dp.toPx()
            val arcSize = Size(size.toPx() - strokeWidth * 2, size.toPx() - strokeWidth * 2)
            val topLeft = Offset(strokeWidth, strokeWidth)

            val startAngle = 140f
            val sweepAngle = 260f

            // 1. Background Arc Track
            drawArc(
                color = Color(0xFF162238),
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )

            // 2. Active Glowing Progress Arc
            val activeSweep = sweepAngle * animatedScore.coerceIn(0.02f, 1f)
            drawArc(
                brush = Brush.sweepGradient(
                    0.0f to CyberCyan,
                    0.5f to CyberGreen,
                    1.0f to CyberCyan
                ),
                startAngle = startAngle,
                sweepAngle = activeSweep,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )

            // 3. Tick Marks & Radial End Cap Glow
            val center = Offset(this.size.width / 2f, this.size.height / 2f)
            val radius = (size.toPx() - strokeWidth * 2) / 2f

            val endRad = Math.toRadians((startAngle + activeSweep).toDouble())
            val capX = center.x + radius * cos(endRad).toFloat()
            val capY = center.y + radius * sin(endRad).toFloat()

            drawCircle(
                color = Color.White,
                radius = 4.dp.toPx(),
                center = Offset(capX, capY)
            )
        }

        // Center Text
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Score",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = TextSecondary
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "$score/$maxScore",
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = FontFamily.Monospace,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                fontSize = 10.sp,
                color = TextMuted
            )
        }
    }
}
