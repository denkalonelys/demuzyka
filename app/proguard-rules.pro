# Add project-specific ProGuard rules here.
# By default the flags in this file are appended to flags specified
# in /home/denis/android-sdk/tools/proguard/proguard-android.txt

# Keep Compose preview functions in debug (preview tooling depends on it).
-keep class androidx.compose.ui.tooling.** { *; }

# Coil uses reflection on response types — keep its public API.
-keep class coil.** { *; }
-dontwarn coil.**

# Media3 / ExoPlayer.
-keep class androidx.media3.** { *; }
-dontwarn androidx.media3.**

# Kotlin metadata for reflection-using libs.
-keep class kotlin.Metadata { *; }
