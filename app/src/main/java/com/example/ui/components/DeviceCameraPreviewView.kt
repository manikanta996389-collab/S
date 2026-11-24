package com.example.ui.components

import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.FocusMeteringAction
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.FallbackStrategy
import androidx.camera.video.FileOutputOptions
import androidx.camera.video.Quality
import androidx.camera.video.QualitySelector
import androidx.camera.video.Recorder
import androidx.camera.video.Recording
import androidx.camera.video.VideoCapture
import androidx.camera.video.VideoRecordEvent
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.ui.theme.TextMuted
import java.io.File

/**
 * CameraX Preview & VideoCapture View embedded into Jetpack Compose for live device CCTV monitoring.
 * Binds to Android lifecycle safely, handles front/rear lens switching, tap-to-focus metering,
 * and MP4 video recording saved directly to internal storage via [FileOutputOptions].
 */
@Composable
fun DeviceCameraPreviewView(
    isFrontCamera: Boolean,
    refocusTrigger: Int = 0,
    isRecordingRequested: Boolean = false,
    recordingOutputFile: File? = null,
    onFocusTriggered: ((Offset) -> Unit)? = null,
    onRecordingStarted: () -> Unit = {},
    onRecordingProgress: (Long) -> Unit = {},
    onRecordingFinalized: (File, Long) -> Unit = { _, _ -> },
    onRecordingError: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var initializationError by remember { mutableStateOf<String?>(null) }
    var activeCamera by remember { mutableStateOf<Camera?>(null) }
    var videoCaptureUseCase by remember { mutableStateOf<VideoCapture<Recorder>?>(null) }
    var activeRecording by remember { mutableStateOf<Recording?>(null) }

    val latestOnRecordingStarted by rememberUpdatedState(onRecordingStarted)
    val latestOnRecordingProgress by rememberUpdatedState(onRecordingProgress)
    val latestOnRecordingFinalized by rememberUpdatedState(onRecordingFinalized)
    val latestOnRecordingError by rememberUpdatedState(onRecordingError)

    val previewView = remember(context) {
        PreviewView(context).apply {
            implementationMode = PreviewView.ImplementationMode.COMPATIBLE
            scaleType = PreviewView.ScaleType.FILL_CENTER
        }
    }

    // Handle manual refocus trigger pulse from HUD
    LaunchedEffect(refocusTrigger) {
        if (refocusTrigger > 0 && activeCamera != null) {
            try {
                val cx = if (previewView.width > 0) previewView.width / 2f else 500f
                val cy = if (previewView.height > 0) previewView.height / 2f else 400f
                val point = previewView.meteringPointFactory.createPoint(cx, cy)
                val action = FocusMeteringAction.Builder(
                    point,
                    FocusMeteringAction.FLAG_AF or FocusMeteringAction.FLAG_AE
                ).build()
                activeCamera?.cameraControl?.startFocusAndMetering(action)
                onFocusTriggered?.invoke(Offset(cx, cy))
            } catch (_: Exception) {}
        }
    }

    // Start or stop CameraX VideoCapture recording based on state
    LaunchedEffect(isRecordingRequested, recordingOutputFile, videoCaptureUseCase) {
        val videoCapture = videoCaptureUseCase
        if (isRecordingRequested && recordingOutputFile != null) {
            if (activeRecording != null) return@LaunchedEffect
            if (videoCapture == null) {
                return@LaunchedEffect
            }
            try {
                recordingOutputFile.parentFile?.mkdirs()
                val fileOutputOptions = FileOutputOptions.Builder(recordingOutputFile).build()
                val executor = ContextCompat.getMainExecutor(context)

                val recording = videoCapture.output
                    .prepareRecording(context, fileOutputOptions)
                    .start(executor) { event ->
                        when (event) {
                            is VideoRecordEvent.Start -> {
                                latestOnRecordingStarted()
                            }
                            is VideoRecordEvent.Status -> {
                                val durationMs = event.recordingStats.recordedDurationNanos / 1_000_000L
                                latestOnRecordingProgress(durationMs)
                            }
                            is VideoRecordEvent.Finalize -> {
                                activeRecording = null
                                val durationMs = event.recordingStats.recordedDurationNanos / 1_000_000L
                                if (!event.hasError()) {
                                    latestOnRecordingFinalized(recordingOutputFile, durationMs)
                                } else {
                                    // Remove empty/corrupted partial file if recording failed
                                    if (recordingOutputFile.exists() && recordingOutputFile.length() == 0L) {
                                        recordingOutputFile.delete()
                                    }
                                    latestOnRecordingError("Video capture error (code ${event.error})")
                                }
                            }
                        }
                    }
                activeRecording = recording
            } catch (e: Exception) {
                activeRecording = null
                latestOnRecordingError(e.localizedMessage ?: "Failed to start video recording")
            }
        } else if (!isRecordingRequested && activeRecording != null) {
            try {
                activeRecording?.stop()
            } catch (_: Exception) {
                activeRecording = null
            }
        }
    }

    DisposableEffect(isFrontCamera, lifecycleOwner) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        val executor = ContextCompat.getMainExecutor(context)

        val listener = Runnable {
            try {
                val cameraProvider = cameraProviderFuture.get()
                val preview = Preview.Builder().build().also {
                    it.surfaceProvider = previewView.surfaceProvider
                }

                val qualitySelector = QualitySelector.fromOrderedList(
                    listOf(Quality.HD, Quality.SD, Quality.FHD),
                    FallbackStrategy.lowerQualityOrHigherThan(Quality.SD)
                )
                val recorder = Recorder.Builder()
                    .setQualitySelector(qualitySelector)
                    .build()
                val videoCapture = VideoCapture.withOutput(recorder)

                val cameraSelector = if (isFrontCamera) {
                    CameraSelector.DEFAULT_FRONT_CAMERA
                } else {
                    CameraSelector.DEFAULT_BACK_CAMERA
                }

                cameraProvider.unbindAll()
                val boundCamera = try {
                    val cam = cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        preview,
                        videoCapture
                    )
                    videoCaptureUseCase = videoCapture
                    cam
                } catch (_: Exception) {
                    // Fallback to preview-only if hardware/emulator rejects dual use-cases
                    videoCaptureUseCase = null
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        preview
                    )
                }

                activeCamera = boundCamera
                initializationError = null
            } catch (e: Exception) {
                initializationError = e.localizedMessage ?: "Camera hardware unavailable"
            }
        }

        cameraProviderFuture.addListener(listener, executor)

        onDispose {
            try {
                activeRecording?.stop()
                activeRecording = null
                videoCaptureUseCase = null
                activeCamera = null
                if (cameraProviderFuture.isDone) {
                    cameraProviderFuture.get().unbindAll()
                }
            } catch (_: Exception) {}
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(activeCamera) {
                detectTapGestures { tapOffset ->
                    try {
                        val point = previewView.meteringPointFactory.createPoint(tapOffset.x, tapOffset.y)
                        val action = FocusMeteringAction.Builder(
                            point,
                            FocusMeteringAction.FLAG_AF or FocusMeteringAction.FLAG_AE
                        ).build()
                        activeCamera?.cameraControl?.startFocusAndMetering(action)
                        onFocusTriggered?.invoke(tapOffset)
                    } catch (_: Exception) {}
                }
            }
    ) {
        AndroidView(
            factory = { previewView },
            modifier = Modifier.fillMaxSize()
        )

        if (initializationError != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xCC000000)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Terminal Sensor Feed: $initializationError",
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    color = TextMuted
                )
            }
        }
    }
}
