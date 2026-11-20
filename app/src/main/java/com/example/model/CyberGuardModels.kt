package com.example.model

data class SecurityEvent(
    val id: String,
    val timestamp: String,
    val cameraName: String,
    val eventType: String,
    val severity: EventSeverity,
    val description: String,
    val isAuthorized: Boolean,
    val errorCode: String? = null,
    val isDismissed: Boolean = false,
    val remediationAction: String? = null
)

enum class EventSeverity {
    INFO,
    WARNING,
    ALERT,
    CRITICAL
}

data class CameraZone(
    val id: String,
    val name: String,
    val location: String,
    val resolution: String = "1080p 30fps",
    val encryption: String = "SRTP / TLS 1.3",
    val status: String = "ONLINE SECURED",
    val isNightVision: Boolean = false,
    val isMotionDetected: Boolean = false,
    val detectedLabel: String = "Secure Zone Clear",
    val latitude: Double = 37.7749,
    val longitude: Double = -122.4194,
    val geofenceRadiusMeters: Float = 60f,
    val distanceMeters: Float = 35f,
    val isWithinGeofence: Boolean = true,
    val autoConnectEnabled: Boolean = true
)

data class SecurityLesson(
    val id: Int,
    val moduleNumber: Int,
    val title: String,
    val subtitle: String,
    val category: String,
    val readTime: String,
    val conceptOverview: String,
    val whyItMatters: String,
    val appInventorSteps: List<AppInventorStep>,
    val defensiveRule: String,
    val isCompleted: Boolean = false
)

data class AppInventorStep(
    val stepNumber: Int,
    val componentName: String,
    val actionType: String, // "Add Component", "Set Property", "Block Logic"
    val details: String,
    val securityPurpose: String
)

data class AuditCamera(
    val id: String,
    val name: String,
    val ipAddress: String,
    val port: Int,
    val hasDefaultPassword: Boolean,
    val isUPnPExposed: Boolean,
    val isFirmwareOutdated: Boolean,
    val isEncryptionMissing: Boolean,
    val isVlanMissing: Boolean,
    val cveIdentifier: String? = null
) {
    fun calculateScore(): Int {
        var score = 100
        if (hasDefaultPassword) score -= 30
        if (isUPnPExposed) score -= 20
        if (isFirmwareOutdated) score -= 20
        if (isEncryptionMissing) score -= 20
        if (isVlanMissing) score -= 10
        return score.coerceAtLeast(10)
    }

    fun grade(): String {
        val s = calculateScore()
        return when {
            s >= 90 -> "A"
            s >= 80 -> "B"
            s >= 70 -> "C"
            s >= 50 -> "D"
            else -> "F (Critical Vulnerabilities)"
        }
    }
}

data class VisualBlock(
    val id: String,
    val category: BlockCategory,
    val text: String,
    val description: String,
    val isSelected: Boolean = false
)

enum class BlockCategory {
    EVENT,      // "When Start_Btn.Click", "When MotionDetected"
    CONDITION,  // "If Perimeter Breach", "If Passcode Valid"
    ACTION      // "Sound Defensive Alarm", "Send Encrypted Alert", "Lock Perimeter Gate"
}

data class QuizQuestion(
    val id: Int,
    val question: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String
)

enum class TriageDecision {
    PENDING,
    ESCALATED,
    DISMISSED
}

data class TriageAlert(
    val id: String,
    val title: String,
    val origin: String,
    val timestamp: String,
    val severity: EventSeverity,
    val description: String,
    val shouldEscalate: Boolean, // Correct defensive SOC answer
    val whyEscalateOrDismiss: String,
    val userDecision: TriageDecision = TriageDecision.PENDING,
    val isResolvedCorrectly: Boolean? = null
)

data class AgentProfile(
    val name: String = "Jacob",
    val email: String = "jacob101@infoeclabs.io",
    val title: String = "Alert Investigation Specialist",
    val badge: String = "PRO",
    val level: Int = 10,
    val totalXp: Int = 910,
    val nextLevelXp: Int = 1000,
    val streak: Int = 2,
    val pendingAlerts: Int = 3,
    val isSystemsOnline: Boolean = true
)

enum class PrivacyMaskStyle {
    BLACKOUT,
    REDACTION_STRIPES,
    PIXEL_MOSAIC
}

data class PrivacyMask(
    val id: String,
    val label: String,
    val leftRatio: Float,
    val topRatio: Float,
    val widthRatio: Float,
    val heightRatio: Float,
    val isEnabled: Boolean = true,
    val description: String = "Sensitive area protected from surveillance recording"
)

