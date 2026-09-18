# Hay Day VR Wrapper

This is a companion/wrapper project for a legitimately installed Hay Day app. It does **not** contain Hay Day's APK, source, textures, sounds, or other proprietary assets.

## What is implemented
- Launches the installed Hay Day package (`com.supercell.hayday`)
- Requests Android MediaProjection permission
- Starts a media-projection foreground service
- Requests overlay permission for the future VR cursor/display layer

## Next implementation layer
The VR renderer should consume the MediaProjection frames and render them to a stereoscopic left/right surface. Hand tracking can then map a pinch gesture to Android AccessibilityService gestures.

Android security restrictions mean the wrapper must use user-granted MediaProjection, overlay, and accessibility permissions; it cannot directly modify Hay Day.
