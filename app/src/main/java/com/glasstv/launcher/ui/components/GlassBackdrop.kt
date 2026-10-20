package com.glasstv.launcher.ui.components

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.glasstv.launcher.data.model.BlurIntensity
import com.glasstv.launcher.data.model.LauncherSettings
import com.glasstv.launcher.data.model.ThemeMode
import com.glasstv.launcher.ui.theme.BackgroundDark
import com.glasstv.launcher.ui.theme.BackgroundDeepMidnight
import com.glasstv.launcher.ui.theme.BackgroundOled
import com.glasstv.launcher.ui.theme.OrbCyan
import com.glasstv.launcher.ui.theme.OrbEmerald
import com.glasstv.launcher.ui.theme.OrbRose
import com.glasstv.launcher.ui.theme.OrbSapphire
import com.glasstv.launcher.ui.theme.OrbViolet

@Composable
fun GlassBackdrop(
    settings: LauncherSettings,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val isOled = settings.themeMode == ThemeMode.OLED_BLACK

    val orbMultiplier = when {
        isOled -> 0.15f
        settings.blurIntensity == BlurIntensity.OFF -> 0.4f
        settings.blurIntensity == BlurIntensity.LOW -> 0.8f
        settings.blurIntensity == BlurIntensity.MEDIUM -> 1.15f
        settings.blurIntensity == BlurIntensity.HIGH -> 1.45f
        else -> 1.0f
    }

    val backgroundModifier = if (isOled) {
        Modifier.background(BackgroundOled)
    } else {
        Modifier.background(
            Brush.verticalGradient(
                colors = listOf(
                    BackgroundDark,
                    BackgroundDeepMidnight,
                    Color(0xFF020306)
                )
            )
        )
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .then(backgroundModifier)
    ) {
        if (orbMultiplier > 0f) {
            val blurModifier = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                Modifier.blur(48.dp)
            } else {
                Modifier
            }

            // Luminous Atmospheric Orb Mesh Container
            Box(modifier = Modifier.fillMaxSize().then(blurModifier)) {
                // Orb 1: Electric Sapphire / Azure behind navigation & top-left cards
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    OrbSapphire.copy(alpha = (OrbSapphire.alpha * orbMultiplier).coerceAtMost(1f)),
                                    Color.Transparent
                                ),
                                center = Offset(360f, 180f),
                                radius = 1000f
                            )
                        )
                )

                // Orb 2: Electric Violet / Purple behind clock & top-right area
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    OrbViolet.copy(alpha = (OrbViolet.alpha * orbMultiplier).coerceAtMost(1f)),
                                    Color.Transparent
                                ),
                                center = Offset(1620f, 200f),
                                radius = 950f
                            )
                        )
                )

                // Orb 3: Radiant Cyan / Teal bloom behind quick access control row
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    OrbCyan.copy(alpha = (OrbCyan.alpha * orbMultiplier).coerceAtMost(1f)),
                                    Color.Transparent
                                ),
                                center = Offset(480f, 960f),
                                radius = 900f
                            )
                        )
                )

                // Orb 4: Lush Emerald / Aurora bloom in lower-right
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    OrbEmerald.copy(alpha = (OrbEmerald.alpha * orbMultiplier).coerceAtMost(1f)),
                                    Color.Transparent
                                ),
                                center = Offset(1550f, 980f),
                                radius = 850f
                            )
                        )
                )

                // Orb 5: Warm Rose / Sunset accent in center-right
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    OrbRose.copy(alpha = (OrbRose.alpha * orbMultiplier * 0.7f).coerceAtMost(1f)),
                                    Color.Transparent
                                ),
                                center = Offset(1150f, 520f),
                                radius = 750f
                            )
                        )
                )

                // Orb 6: Luminous Electric Azure/Sapphire bloom in center-left
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    OrbSapphire.copy(alpha = (OrbSapphire.alpha * orbMultiplier * 0.9f).coerceAtMost(1f)),
                                    Color.Transparent
                                ),
                                center = Offset(240f, 560f),
                                radius = 850f
                            )
                        )
                )
            }

            // Top Frosted Glass Horizon Specular Light
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0x30FFFFFF),
                                Color(0x10FFFFFF),
                                Color.Transparent
                            ),
                            startY = 0f,
                            endY = 280f
                        )
                    )
            )
        }

        content()
    }
}

