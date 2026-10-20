package com.glasstv.launcher.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.material3.Icon
import androidx.tv.material3.Text
import com.glasstv.launcher.data.model.SystemShortcut
import com.glasstv.launcher.ui.theme.AccentAmber
import com.glasstv.launcher.ui.theme.AccentCyan
import com.glasstv.launcher.ui.theme.AccentEmerald
import com.glasstv.launcher.ui.theme.AccentPurple
import com.glasstv.launcher.ui.theme.Dimens
import com.glasstv.launcher.ui.theme.GlassBorderFocusedGradient
import com.glasstv.launcher.ui.theme.GlassBorderGradient
import com.glasstv.launcher.ui.theme.GlassSpecularSheen
import com.glasstv.launcher.ui.theme.TextPrimary
import com.glasstv.launcher.ui.theme.TextSecondary

@Composable
fun ShortcutTile(
    shortcut: SystemShortcut,
    onClick: (SystemShortcut) -> Unit,
    modifier: Modifier = Modifier,
    isReducedMotion: Boolean = false
) {
    var isFocused by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }

    val targetScale = when {
        isFocused && isReducedMotion -> Dimens.FocusScaleReduced
        isFocused -> Dimens.FocusScaleDefault
        else -> 1.0f
    }

    val scale by animateFloatAsState(
        targetValue = targetScale,
        animationSpec = androidx.compose.animation.core.tween(
            durationMillis = 110,
            easing = androidx.compose.animation.core.FastOutSlowInEasing
        ),
        label = "shortcut_scale"
    )

    val shape = RoundedCornerShape(20.dp)
    val config = getShortcutConfig(shortcut.id)

    Box(
        modifier = modifier
            .padding(horizontal = 6.dp, vertical = 6.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .size(width = 186.dp, height = 76.dp)
            .shadow(
                elevation = if (isFocused) 12.dp else 4.dp,
                shape = shape,
                spotColor = if (isFocused) config.accentColor.copy(alpha = 0.55f) else Color(0x50000000)
            )
            .clip(shape)
            // Translucent glass foundation
            .background(if (isFocused) Color(0x351E293B) else Color(0x280F172A))
            // Frosted glass refraction fill
            .background(
                Brush.verticalGradient(
                    colors = if (isFocused) {
                        listOf(
                            Color(0x32FFFFFF),
                            Color(0x14FFFFFF),
                            Color(0x06000000)
                        )
                    } else {
                        listOf(
                            Color(0x28FFFFFF),
                            Color(0x10FFFFFF),
                            Color(0x04000000)
                        )
                    }
                )
            )
            // Specular gradient border
            .border(
                border = BorderStroke(
                    width = if (isFocused) 1.5.dp else 1.dp,
                    brush = if (isFocused) GlassBorderFocusedGradient else GlassBorderGradient
                ),
                shape = shape
            )
            .onFocusChanged { isFocused = it.isFocused }
            .focusable(interactionSource = interactionSource)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = { onClick(shortcut) }
            ),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 14.dp)
        ) {
            // Glowing Tinted Glass Icon Badge
            val badgeShape = RoundedCornerShape(14.dp)
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .shadow(
                        elevation = if (isFocused) 10.dp else 4.dp,
                        shape = badgeShape,
                        spotColor = config.accentColor
                    )
                    .clip(badgeShape)
                    .background(
                        Brush.linearGradient(
                            colors = config.gradientColors
                        )
                    )
                    .border(
                        BorderStroke(1.dp, Color(0x80FFFFFF)),
                        badgeShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = config.icon,
                    contentDescription = shortcut.title,
                    tint = Color.White,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Two-Line Modern Typography
            Column(verticalArrangement = Arrangement.Center) {
                Text(
                    text = shortcut.title,
                    color = TextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.2.sp
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = config.subtitle,
                    color = if (isFocused) AccentCyan else TextSecondary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // Top-Down Specular Glass Reflection Sheen on Focus
        if (isFocused) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(GlassSpecularSheen)
            )
        }
    }
}

private data class ShortcutConfig(
    val icon: ImageVector,
    val subtitle: String,
    val accentColor: Color,
    val gradientColors: List<Color>
)

private fun getShortcutConfig(shortcutId: String): ShortcutConfig {
    return when (shortcutId) {
        "settings" -> ShortcutConfig(
            icon = Icons.Default.Settings,
            subtitle = "System Setup",
            accentColor = AccentCyan,
            gradientColors = listOf(Color(0xFF06B6D4), Color(0xFF0284C7))
        )
        "network" -> ShortcutConfig(
            icon = Icons.Default.Wifi,
            subtitle = "Wi-Fi & LAN",
            accentColor = Color(0xFF6366F1),
            gradientColors = listOf(Color(0xFF6366F1), Color(0xFF4338CA))
        )
        "sound" -> ShortcutConfig(
            icon = Icons.Default.VolumeUp,
            subtitle = "Audio Output",
            accentColor = AccentPurple,
            gradientColors = listOf(Color(0xFFA855F7), Color(0xFF7E22CE))
        )
        "apps" -> ShortcutConfig(
            icon = Icons.Default.Apps,
            subtitle = "App Manager",
            accentColor = AccentAmber,
            gradientColors = listOf(Color(0xFFF59E0B), Color(0xFFD97706))
        )
        "home_settings" -> ShortcutConfig(
            icon = Icons.Default.Home,
            subtitle = "Home Launcher",
            accentColor = AccentEmerald,
            gradientColors = listOf(Color(0xFF10B981), Color(0xFF047857))
        )
        else -> ShortcutConfig(
            icon = Icons.Default.Settings,
            subtitle = "Preferences",
            accentColor = AccentCyan,
            gradientColors = listOf(Color(0xFF06B6D4), Color(0xFF0284C7))
        )
    }
}
