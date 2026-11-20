package com.example

import com.example.model.AuditCamera
import com.example.model.EventSeverity
import com.example.viewmodel.CyberGuardViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleRobolectricTest {

  @Test
  fun `audit camera calculates vulnerable score correctly`() {
    val vulnerableCamera = AuditCamera(
      id = "test_cam",
      name = "Test Porch Cam",
      ipAddress = "192.168.1.50",
      port = 554,
      hasDefaultPassword = true,
      isUPnPExposed = true,
      isFirmwareOutdated = true,
      isEncryptionMissing = true,
      isVlanMissing = true
    )
    assertEquals(10, vulnerableCamera.calculateScore())
    assertTrue(vulnerableCamera.grade().startsWith("F"))
  }

  @Test
  fun `geolocation auto-connects to closest camera zone`() {
    val viewModel = CyberGuardViewModel()
    
    // Simulate user standing right at Zone 2 (Server Vault) coordinates
    viewModel.simulateLocationPreset("ZONE_2")
    
    val state = viewModel.uiState.value
    assertEquals(1, state.selectedZoneIndex)
    assertEquals("Zone 2: Data Server Vault", state.cameraZones[state.selectedZoneIndex].name)
    assertTrue(state.cameraZones[1].isWithinGeofence)
  }

  @Test
  fun `triggering security error adds error event with error code`() {
    val viewModel = CyberGuardViewModel()
    viewModel.triggerSecurityError("CIPHER_ERROR")

    val state = viewModel.uiState.value
    val latestEvent = state.events.first()
    assertEquals(EventSeverity.CRITICAL, latestEvent.severity)
    assertEquals("ERR_STREAM_CIPHER_0x502", latestEvent.errorCode)
    assertNotNull(latestEvent.remediationAction)
  }

  @Test
  fun `muting alerts disables new alert logging`() {
    val viewModel = CyberGuardViewModel()
    viewModel.toggleMuteAlerts()
    assertTrue(viewModel.uiState.value.areAlertsMuted)

    val countBefore = viewModel.uiState.value.events.size
    viewModel.triggerSecurityError("AUTH_ERROR")
    val countAfter = viewModel.uiState.value.events.size
    assertEquals(countBefore, countAfter)
  }

  @Test
  fun `disabling alert rule filters out that event type`() {
    val viewModel = CyberGuardViewModel()
    val eventTypeToDisable = "CRITICAL: RTSP Stream Cipher Tamper"
    viewModel.toggleDisableAlertRule(eventTypeToDisable)

    assertTrue(viewModel.uiState.value.disabledEventTypes.contains(eventTypeToDisable))
    assertFalse(viewModel.uiState.value.filteredEvents().any { it.eventType == eventTypeToDisable })
  }

  @Test
  fun `clearAllAlerts removes all events and triage alerts`() {
    val viewModel = CyberGuardViewModel()
    assertTrue(viewModel.uiState.value.events.isNotEmpty())
    assertTrue(viewModel.uiState.value.triageAlerts.isNotEmpty())

    viewModel.clearAllAlerts()

    assertTrue(viewModel.uiState.value.events.isEmpty())
    assertTrue(viewModel.uiState.value.triageAlerts.isEmpty())
    assertEquals(0, viewModel.uiState.value.agentProfile.pendingAlerts)
  }

  @Test
  fun `removeTriageAlert removes specific alert from queue`() {
    val viewModel = CyberGuardViewModel()
    val initialSize = viewModel.uiState.value.triageAlerts.size
    val firstId = viewModel.uiState.value.triageAlerts.first().id

    viewModel.removeTriageAlert(firstId)

    assertEquals(initialSize - 1, viewModel.uiState.value.triageAlerts.size)
    assertFalse(viewModel.uiState.value.triageAlerts.any { it.id == firstId })
  }

  @Test
  fun `location service coordinates model stores latitude and longitude`() {
    val coords = com.example.data.LocationService.Coordinates(
      latitude = 37.77492,
      longitude = -122.41945,
      accuracyMeters = 5.0f,
      provider = "fused_play_services"
    )
    assertEquals(37.77492, coords.latitude, 0.0001)
    assertEquals(-122.41945, coords.longitude, 0.0001)
    assertEquals("fused_play_services", coords.provider)
  }

  @Test
  fun `local proxy server lifecycle and port change`() {
    val proxy = com.example.data.LocalProxyServer(defaultPort = 8089)
    assertEquals(8089, proxy.currentPort)

    val started = proxy.start()
    assertTrue(started)
    assertTrue(proxy.stats.value.isRunning)

    proxy.setPort(8091)
    assertEquals(8091, proxy.currentPort)
    assertTrue(proxy.stats.value.isRunning)

    proxy.stop()
    assertFalse(proxy.stats.value.isRunning)
  }

  @Test
  fun `viewModel proxy server state and probe toggle`() {
    val viewModel = CyberGuardViewModel()
    assertTrue(viewModel.uiState.value.isProxyServerActive)
    assertEquals(8080, viewModel.uiState.value.proxyPort)

    viewModel.setProxyPort(8443)
    assertEquals(8443, viewModel.uiState.value.proxyPort)

    viewModel.dismissProxyTestResult()
    assertEquals(null, viewModel.uiState.value.proxyTestResult)
  }

  @Test
  fun `viewModel device camera permission and lens toggle`() {
    val viewModel = CyberGuardViewModel()
    assertFalse(viewModel.uiState.value.isCameraPermissionGranted)
    assertFalse(viewModel.uiState.value.isDeviceCameraActive)
    assertFalse(viewModel.uiState.value.cameraFacingFront)

    // Grant permission
    viewModel.setCameraPermission(true)
    assertTrue(viewModel.uiState.value.isCameraPermissionGranted)

    // Activate device camera
    viewModel.toggleDeviceCamera(true)
    assertTrue(viewModel.uiState.value.isDeviceCameraActive)

    // Switch lens
    viewModel.toggleCameraFacing()
    assertTrue(viewModel.uiState.value.cameraFacingFront)

    // Revoking permission should disable device camera
    viewModel.setCameraPermission(false)
    assertFalse(viewModel.uiState.value.isCameraPermissionGranted)
    assertFalse(viewModel.uiState.value.isDeviceCameraActive)
  }

  @Test
  fun `viewModel privacy masking state, toggle, and styles`() {
    val viewModel = CyberGuardViewModel()
    assertTrue(viewModel.uiState.value.isPrivacyMaskingEnabled)
    assertEquals(com.example.model.PrivacyMaskStyle.BLACKOUT, viewModel.uiState.value.privacyMaskStyle)
    assertTrue(viewModel.uiState.value.privacyMasks.isNotEmpty())

    // Toggle master privacy masking
    viewModel.togglePrivacyMasking()
    assertFalse(viewModel.uiState.value.isPrivacyMaskingEnabled)

    viewModel.togglePrivacyMasking()
    assertTrue(viewModel.uiState.value.isPrivacyMaskingEnabled)

    // Toggle individual mask
    val keypadMask = viewModel.uiState.value.privacyMasks.first { it.id == "mask_keypad" }
    assertTrue(keypadMask.isEnabled)
    viewModel.togglePrivacyMask("mask_keypad")
    assertFalse(viewModel.uiState.value.privacyMasks.first { it.id == "mask_keypad" }.isEnabled)

    // Switch style
    viewModel.setPrivacyMaskStyle(com.example.model.PrivacyMaskStyle.PIXEL_MOSAIC)
    assertEquals(com.example.model.PrivacyMaskStyle.PIXEL_MOSAIC, viewModel.uiState.value.privacyMaskStyle)

    // Add custom mask
    val initialCount = viewModel.uiState.value.privacyMasks.size
    viewModel.addCustomPrivacyMask("Test Cash Register")
    assertEquals(initialCount + 1, viewModel.uiState.value.privacyMasks.size)
    assertTrue(viewModel.uiState.value.privacyMasks.any { it.label == "Test Cash Register" })

    // Reset defaults
    viewModel.resetDefaultPrivacyMasks()
    assertEquals(3, viewModel.uiState.value.privacyMasks.size)
    assertEquals(com.example.model.PrivacyMaskStyle.BLACKOUT, viewModel.uiState.value.privacyMaskStyle)
    val windowMask = viewModel.uiState.value.privacyMasks.first { it.id == "mask_window" }
    assertEquals(0.22f, windowMask.topRatio, 0.01f)
  }

  @Test
  fun `viewModel clarity filter, autofocus pulse, and blur diagnostic state`() {
    val viewModel = CyberGuardViewModel()
    assertFalse(viewModel.uiState.value.isClarityFilterActive)
    assertEquals(0, viewModel.uiState.value.refocusPulseCount)
    assertFalse(viewModel.uiState.value.isBlurDiagnosticOpen)

    // Toggle clarity filter
    viewModel.toggleClarityFilter()
    assertTrue(viewModel.uiState.value.isClarityFilterActive)
    viewModel.toggleClarityFilter()
    assertFalse(viewModel.uiState.value.isClarityFilterActive)

    // Trigger autofocus pulse
    viewModel.triggerRefocusPulse()
    assertEquals(1, viewModel.uiState.value.refocusPulseCount)
    viewModel.triggerRefocusPulse()
    assertEquals(2, viewModel.uiState.value.refocusPulseCount)

    // Toggle blur diagnostic modal
    viewModel.toggleBlurDiagnostic(true)
    assertTrue(viewModel.uiState.value.isBlurDiagnosticOpen)
    viewModel.toggleBlurDiagnostic(false)
    assertFalse(viewModel.uiState.value.isBlurDiagnosticOpen)
  }

  @Test
  fun `triggering BLUR_TAMPER logs ERR_LENS_DEFOCUS_0x301 with optical remediation`() {
    val viewModel = CyberGuardViewModel()
    viewModel.triggerSecurityError("BLUR_TAMPER")

    val state = viewModel.uiState.value
    val blurEvent = state.events.first()
    assertEquals(EventSeverity.CRITICAL, blurEvent.severity)
    assertEquals("ERR_LENS_DEFOCUS_0x301", blurEvent.errorCode)
    assertEquals("CRITICAL: CCTV Optical Blur / Lens Defocus", blurEvent.eventType)
    assertNotNull(blurEvent.remediationAction)
    assertTrue(blurEvent.remediationAction!!.contains("auto-refocus") || blurEvent.remediationAction!!.contains("Clarity"))
  }
}
