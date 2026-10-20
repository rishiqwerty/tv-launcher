package com.glasstv.launcher.data.model

enum class ThemeMode {
    DARK,
    OLED_BLACK,
    SUBTLE_GRADIENT
}

enum class BlurIntensity {
    OFF,
    LOW,
    MEDIUM,
    HIGH
}

enum class UiDensity {
    COMFORTABLE,
    COMPACT
}

data class LauncherSettings(
    val themeMode: ThemeMode = ThemeMode.DARK,
    val blurIntensity: BlurIntensity = BlurIntensity.MEDIUM,
    val uiDensity: UiDensity = UiDensity.COMFORTABLE,
    val reducedMotion: Boolean = false,
    val showClock: Boolean = true,
    val clockFormat24Hour: Boolean = false,
    val includeMobileApps: Boolean = true
)
