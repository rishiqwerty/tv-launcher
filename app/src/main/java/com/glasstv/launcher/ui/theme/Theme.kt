package com.glasstv.launcher.ui.theme

import androidx.compose.runtime.Composable
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.darkColorScheme
import com.glasstv.launcher.data.model.LauncherSettings
import com.glasstv.launcher.data.model.ThemeMode

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun GlassTVTheme(
    settings: LauncherSettings = LauncherSettings(),
    content: @Composable () -> Unit
) {
    val colorScheme = when (settings.themeMode) {
        ThemeMode.OLED_BLACK -> darkColorScheme(
            background = BackgroundOled,
            surface = BackgroundOled,
            primary = AccentCyan
        )
        ThemeMode.SUBTLE_GRADIENT -> darkColorScheme(
            background = BackgroundGradientStart,
            surface = GlassCardBase,
            primary = AccentBlue
        )
        ThemeMode.DARK -> darkColorScheme(
            background = BackgroundDark,
            surface = GlassCardBase,
            primary = AccentCyan
        )
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
