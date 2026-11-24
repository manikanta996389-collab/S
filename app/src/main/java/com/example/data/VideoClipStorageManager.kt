package com.example.data

import android.content.Context
import android.media.MediaMetadataRetriever
import com.example.model.ClipNamingPattern
import com.example.model.SavedVideoClip
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Manages saving, naming, listing, and deleting CCTV video clips in app-private
 * internal storage (`context.filesDir/cctv_clips`).
 *
 * Storing clips in internal storage requires zero external storage permissions
 * and keeps recorded surveillance footage private to this application.
 */
class VideoClipStorageManager(
    private val clipsDirectory: File
) {
    constructor(context: Context) : this(File(context.filesDir, CLIPS_DIR_NAME))

    init {
        if (!clipsDirectory.exists()) {
            clipsDirectory.mkdirs()
        }
    }

    fun getStorageDirectoryPath(): String = clipsDirectory.absolutePath

    /**
     * Creates a unique output [File] inside internal storage following the user's
     * custom naming convention and timestamp format.
     */
    fun createClipOutputFile(
        customPrefix: String,
        cameraSourceTag: String,
        pattern: ClipNamingPattern,
        timestampMillis: Long = System.currentTimeMillis()
    ): File {
        if (!clipsDirectory.exists()) {
            clipsDirectory.mkdirs()
        }

        val baseFileName = buildClipFileName(
            customPrefix = customPrefix,
            cameraSourceTag = cameraSourceTag,
            pattern = pattern,
            timestampMillis = timestampMillis
        )

        var candidate = File(clipsDirectory, baseFileName)
        if (!candidate.exists()) {
            return candidate
        }

        // Prevent filename collisions when multiple clips are started within the same second
        val nameWithoutExt = baseFileName.removeSuffix(".mp4")
        var counter = 1
        while (candidate.exists()) {
            val suffix = String.format(Locale.US, "_%02d.mp4", counter)
            candidate = File(clipsDirectory, "$nameWithoutExt$suffix")
            counter++
        }
        return candidate
    }

    /**
     * Scans internal storage and returns all saved MP4 clips sorted newest-first.
     */
    fun listSavedClips(): List<SavedVideoClip> {
        if (!clipsDirectory.exists()) return emptyList()

        val files = clipsDirectory.listFiles { file ->
            file.isFile && file.name.endsWith(".mp4", ignoreCase = true) && file.length() > 0L
        } ?: return emptyList()

        return files
            .sortedByDescending { it.lastModified() }
            .map { file ->
                val durationMs = extractDurationMillis(file)
                val sourceLabel = inferSourceLabel(file.name)
                SavedVideoClip(
                    id = file.name,
                    fileName = file.name,
                    filePath = file.absolutePath,
                    sizeBytes = file.length(),
                    durationMs = durationMs,
                    createdAtMillis = file.lastModified(),
                    sourceLabel = sourceLabel
                )
            }
    }

    /**
     * Safely deletes a single clip file if and only if it belongs inside the managed directory.
     */
    fun deleteClip(filePath: String): Boolean {
        val target = File(filePath)
        val canonicalDir = runCatching { clipsDirectory.canonicalFile }.getOrElse { clipsDirectory }
        val canonicalTarget = runCatching { target.canonicalFile }.getOrElse { target }
        if (canonicalTarget.parentFile != canonicalDir) {
            return false
        }
        return canonicalTarget.exists() && canonicalTarget.delete()
    }

    /**
     * Deletes all saved clips inside the internal CCTV vault directory.
     */
    fun clearAllClips(): Int {
        if (!clipsDirectory.exists()) return 0
        var deletedCount = 0
        clipsDirectory.listFiles()?.forEach { file ->
            if (file.isFile && file.name.endsWith(".mp4", ignoreCase = true) && file.delete()) {
                deletedCount++
            }
        }
        return deletedCount
    }

    fun getTotalStorageBytes(): Long {
        if (!clipsDirectory.exists()) return 0L
        return clipsDirectory.listFiles()
            ?.filter { it.isFile && it.name.endsWith(".mp4", ignoreCase = true) }
            ?.sumOf { it.length() }
            ?: 0L
    }

    private fun extractDurationMillis(file: File): Long {
        return try {
            val retriever = MediaMetadataRetriever()
            try {
                retriever.setDataSource(file.absolutePath)
                retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLongOrNull() ?: 0L
            } finally {
                retriever.release()
            }
        } catch (_: Throwable) {
            0L
        }
    }

    companion object {
        const val CLIPS_DIR_NAME = "cctv_clips"
        private const val DEFAULT_PREFIX = "CCTV"
        private const val DEFAULT_SOURCE = "CAM"

        /**
         * Sanitizes a user-provided prefix or camera tag into safe uppercase filename tokens.
         */
        fun sanitizeToken(raw: String, fallback: String): String {
            val cleaned = raw
                .trim()
                .uppercase(Locale.US)
                .replace(Regex("[^A-Z0-9_-]+"), "_")
                .replace(Regex("_+"), "_")
                .trim('_', '-')
                .take(20)
            return cleaned.ifEmpty { fallback }
        }

        /**
         * Generates a custom filename with embedded timestamp according to the selected [ClipNamingPattern].
         */
        fun buildClipFileName(
            customPrefix: String,
            cameraSourceTag: String,
            pattern: ClipNamingPattern,
            timestampMillis: Long = System.currentTimeMillis()
        ): String {
            val prefix = sanitizeToken(customPrefix, DEFAULT_PREFIX)
            val source = sanitizeToken(cameraSourceTag, DEFAULT_SOURCE)
            val date = Date(timestampMillis)

            return when (pattern) {
                ClipNamingPattern.STANDARD -> {
                    val ts = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(date)
                    "${prefix}_${source}_${ts}.mp4"
                }
                ClipNamingPattern.ISO_TIMESTAMP -> {
                    val ts = SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.US).format(date)
                    "${prefix}_${ts}_${source}.mp4"
                }
                ClipNamingPattern.INCIDENT_LOG -> {
                    val ts = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(date)
                    "INC_${prefix}_${source}_${ts}.mp4"
                }
            }
        }

        fun inferSourceLabel(fileName: String): String {
            val upper = fileName.uppercase(Locale.US)
            return when {
                upper.contains("FRONT") -> "Device Cam (Front)"
                upper.contains("REAR") -> "Device Cam (Rear)"
                upper.contains("ZONE1") -> "Zone 1: Front Entrance"
                upper.contains("ZONE2") -> "Zone 2: Data Vault"
                upper.contains("ZONE3") -> "Zone 3: Logistics Dock"
                else -> "Local CCTV Sensor"
            }
        }

        fun formatFileSize(bytes: Long): String {
            return when {
                bytes < 1024L -> "$bytes B"
                bytes < 1024L * 1024L -> String.format(Locale.US, "%.1f KB", bytes / 1024.0)
                else -> String.format(Locale.US, "%.2f MB", bytes / (1024.0 * 1024.0))
            }
        }

        fun formatDuration(durationMs: Long): String {
            val totalSeconds = (durationMs / 1000L).coerceAtLeast(0L)
            val minutes = totalSeconds / 60L
            val seconds = totalSeconds % 60L
            return String.format(Locale.US, "%02d:%02d", minutes, seconds)
        }

        fun formatTimestampDisplay(millis: Long): String {
            return SimpleDateFormat("MMM dd, HH:mm:ss", Locale.US).format(Date(millis))
        }
    }
}
