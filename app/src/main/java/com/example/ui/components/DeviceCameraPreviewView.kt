package com.example.ui.components

import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.FocusMeteringAction
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
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

/**
 * CameraX Preview View embedded into Jetpack Compose for live device CCTV monitoring.
 * Binds to Android lifecycle safely, handles camera lens selection, and supports
 * tap-to-focus and manual autofocus sweep metering to resolve blurry feeds.
 */
@Composable
fun DeviceCameraPreviewView(
    isFrontCamera: Boolean,
    refocusTrigger: Int = 0,
    onFocusTriggered: ((Offset) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var initializationError by remember { mutableStateOf<String?>(null) }
    var activeCamera by remember { mutableStateOf<Camera?>(null) }

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

    DisposableEffect(isFrontCamera, lifecycleOwner) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        val executor = ContextCompat.getMainExecutor(context)

        val listener = Runnable {
            try {
                val cameraProvider = cameraProviderFuture.get()
                val preview = Preview.Builder().build().also {
                    it.surfaceProvider = previewView.surfaceProvider
                }

                val cameraSelector = if (isFrontCamera) {
                    CameraSelector.DEFAULT_FRONT_CAMERA
                } else {
                    CameraSelector.DEFAULT_BACK_CAMERA
                }

                cameraProvider.unbindAll()
                val boundCamera = cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    preview
                )
                activeCamera = boundCamera
                initializationError = null
            } catch (e: Exception) {
                initializationError = e.localizedMessage ?: "Camera hardware unavailable"
            }
        }

        cameraProviderFuture.addListener(listener, executor)

        onDispose {
            try {
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
                    text = "Terminal Sensor Feed: ${initializationError}",
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    color = TextMuted
                )
            }
        }
    }
}
