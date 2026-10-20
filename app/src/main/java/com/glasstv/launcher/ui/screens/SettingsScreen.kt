package com.glasstv.launcher.ui.screens

import android.content.Context
import android.content.Intent
import android.provider.Settings
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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Visibility
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.material3.Icon
import androidx.tv.material3.Text
import com.glasstv.launcher.data.model.BlurIntensity
import com.glasstv.launcher.data.model.LauncherSettings
import com.glasstv.launcher.data.model.ThemeMode
import com.glasstv.launcher.ui.components.GlassBackdrop
import com.glasstv.launcher.ui.components.LauncherScreen
import com.glasstv.launcher.ui.components.TopNavBar
import com.glasstv.launcher.ui.theme.AccentAmber
import com.glasstv.launcher.ui.theme.AccentCyan
import com.glasstv.launcher.ui.theme.AccentEmerald
import com.glasstv.launcher.ui.theme.AccentPurple
import com.glasstv.launcher.ui.theme.Dimens
import com.glasstv.launcher.ui.theme.GlassBorderFocusedGradient
import com.glasstv.launcher.ui.theme.GlassBorderGradient
import com.glasstv.launcher.ui.theme.GlassCardFillGradient
import com.glasstv.launcher.ui.theme.GlassCardFocusedGradient
import com.glasstv.launcher.ui.theme.GlassSpecularSheen
import com.glasstv.launcher.ui.theme.TextMuted
import com.glasstv.launcher.ui.theme.TextPrimary
import com.glasstv.launcher.ui.theme.TextSecondary
import com.glasstv.launcher.ui.viewmodel.HomeUiState

@Composable
fun SettingsScreen(
    uiState: HomeUiState,
    onUpdateSettings: ((LauncherSettings) -> LauncherSettings) -> Unit,
    onScreenSelected: (LauncherScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val settings = uiState.settings

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    horizontal = Dimens.ScreenHorizontalPadding,
                    vertical = Dimens.ScreenVerticalPadding
                )
                .verticalScroll(rememberScrollState())
        ) {
            // Top Navigation Bar
            TopNavBar(
                currentScreen = LauncherScreen.SETTINGS,
                onScreenSelected = onScreenSelected,
                showClock = settings.showClock,
                is24Hour = settings.clockFormat24Hour
            )

            Spacer(modifier = Modifier.height(Dimens.SectionSpacing))

            // 1. Appearance Section
            SettingsSectionHeader(title = "APPEARANCE", dotColor = AccentCyan)

            SettingsRow(
                icon = Icons.Default.Palette,
                iconGradient = listOf(Color(0xFF6366F1), Color(0xFF4338CA)),
                accentColor = Color(0xFF6366F1),
                title = "Theme Atmosphere",
                subtitle = "Select atmospheric glass backdrop rendering mode",
                badgeValue = when (settings.themeMode) {
                    ThemeMode.DARK -> "DARK GLASS"
                    ThemeMode.OLED_BLACK -> "OLED BLACK"
                    ThemeMode.SUBTLE_GRADIENT -> "SUBTLE GRADIENT"
                },
                onClick = {
                    val nextTheme = when (settings.themeMode) {
                        ThemeMode.DARK -> ThemeMode.OLED_BLACK
                        ThemeMode.OLED_BLACK -> ThemeMode.SUBTLE_GRADIENT
                        ThemeMode.SUBTLE_GRADIENT -> ThemeMode.DARK
                    }
                    onUpdateSettings { it.copy(themeMode = nextTheme) }
                }
            )

            SettingsRow(
                icon = Icons.Default.AutoAwesome,
                iconGradient = listOf(Color(0xFF06B6D4), Color(0xFF0284C7)),
                accentColor = AccentCyan,
                title = "Frosted Glass Depth",
                subtitle = "Hardware blur and luminous ambient orbs intensity",
                badgeValue = settings.blurIntensity.name,
                onClick = {
                    val nextBlur = when (settings.blurIntensity) {
                        BlurIntensity.OFF -> BlurIntensity.LOW
                        BlurIntensity.LOW -> BlurIntensity.MEDIUM
                        BlurIntensity.MEDIUM -> BlurIntensity.HIGH
                        BlurIntensity.HIGH -> BlurIntensity.OFF
                    }
                    onUpdateSettings { it.copy(blurIntensity = nextBlur) }
                }
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 2. Behavior & Accessibility
            SettingsSectionHeader(title = "BEHAVIOR & ACCESSIBILITY", dotColor = AccentPurple)

            SettingsRow(
                icon = Icons.Default.Speed,
                iconGradient = listOf(Color(0xFFF59E0B), Color(0xFFD97706)),
                accentColor = AccentAmber,
                title = "Reduced Motion",
                subtitle = if (settings.reducedMotion) "Minimal scale & focus motion enabled" else "Smooth spring physics and scale transitions",
                badgeValue = if (settings.reducedMotion) "REDUCED" else "DYNAMIC",
                onClick = {
                    onUpdateSettings { it.copy(reducedMotion = !it.reducedMotion) }
                }
            )

            SettingsRow(
                icon = Icons.Default.AccessTime,
                iconGradient = listOf(Color(0xFF10B981), Color(0xFF047857)),
                accentColor = AccentEmerald,
                title = "Clock Time Format",
                subtitle = if (settings.clockFormat24Hour) "24-Hour digital format (e.g. 17:30)" else "12-Hour standard format (e.g. 5:30 PM)",
                badgeValue = if (settings.clockFormat24Hour) "24-HOUR" else "12-HOUR",
                onClick = {
                    onUpdateSettings { it.copy(clockFormat24Hour = !it.clockFormat24Hour) }
                }
            )

            SettingsRow(
                icon = Icons.Default.Visibility,
                iconGradient = listOf(Color(0xFF38BDF8), Color(0xFF0284C7)),
                accentColor = AccentCyan,
                title = "Show Clock & Date Pill",
                subtitle = "Toggle floating glass clock capsule visibility in top bar",
                badgeValue = if (settings.showClock) "VISIBLE" else "HIDDEN",
                onClick = {
                    onUpdateSettings { it.copy(showClock = !it.showClock) }
                }
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 3. System Launcher Setup
            SettingsSectionHeader(title = "SYSTEM LAUNCHER SETUP", dotColor = AccentCyan)

            SettingsRow(
                icon = Icons.Default.Home,
                iconGradient = listOf(Color(0xFFA855F7), Color(0xFF7E22CE)),
                accentColor = AccentPurple,
                title = "Set as Default Home Launcher",
                subtitle = "Configure Android TV system settings to make TV Launcher default",
                badgeValue = "OPEN SETUP",
                onClick = {
                    try {
                        val intent = Intent(Settings.ACTION_HOME_SETTINGS).apply {
                            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        }
                        context.startActivity(intent)
                    } catch (e: Exception) {
                        val fallback = Intent(Settings.ACTION_SETTINGS).apply {
                            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        }
                        context.startActivity(fallback)
                    }
                }
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 4. About Section
            SettingsSectionHeader(title = "ABOUT", dotColor = TextSecondary)

            SettingsRow(
                icon = Icons.Default.Info,
                iconGradient = listOf(Color(0xFF64748B), Color(0xFF334155)),
                accentColor = TextSecondary,
                title = "TV Launcher",
                subtitle = "Version 1.0.0 • Offline-First • Zero Ads • Apple TV Aesthetics",
                badgeValue = "v1.0.0",
                onClick = {}
            )

            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}

@Composable
private fun SettingsSectionHeader(title: String, dotColor: Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(start = 8.dp, bottom = 12.dp)
    ) {
        // Glowing vertical accent beacon
        Box(
            modifier = Modifier
                .size(width = 4.dp, height = 15.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(dotColor)
                .shadow(6.dp, RoundedCornerShape(2.dp), spotColor = dotColor)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = title,
            color = TextPrimary,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 2.sp
        )
    }
}

@Composable
private fun SettingsRow(
    icon: ImageVector,
    iconGradient: List<Color>,
    accentColor: Color,
    title: String,
    subtitle: String,
    badgeValue: String,
    onClick: () -> Unit
) {
    var isFocused by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }

    val scale by animateFloatAsState(
        targetValue = if (isFocused) 1.025f else 1.0f,
        animationSpec = androidx.compose.animation.core.tween(
            durationMillis = 110,
            easing = androidx.compose.animation.core.FastOutSlowInEasing
        ),
        label = "settings_row_scale"
    )

    val shape = RoundedCornerShape(18.dp)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .shadow(
                elevation = if (isFocused) 16.dp else 4.dp,
                shape = shape,
                spotColor = if (isFocused) accentColor else Color(0x60000000)
            )
            .clip(shape)
            .background(if (isFocused) Color(0x451E293B) else Color(0x350F172A))
            .background(if (isFocused) GlassCardFocusedGradient else GlassCardFillGradient)
            .border(
                border = BorderStroke(
                    width = if (isFocused) 2.dp else 1.dp,
                    brush = if (isFocused) GlassBorderFocusedGradient else GlassBorderGradient
                ),
                shape = shape
            )
            .onFocusChanged { isFocused = it.isFocused }
            .focusable(interactionSource = interactionSource)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .padding(horizontal = 20.dp, vertical = 14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                // Vibrant Frosted Glass Icon Badge
                val badgeShape = RoundedCornerShape(14.dp)
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .shadow(
                            elevation = if (isFocused) 10.dp else 3.dp,
                            shape = badgeShape,
                            spotColor = accentColor
                        )
                        .clip(badgeShape)
                        .background(Brush.linearGradient(iconGradient))
                        .border(BorderStroke(1.dp, Color(0x80FFFFFF)), badgeShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = title,
                        color = TextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.2.sp
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = subtitle,
                        color = if (isFocused) Color.White else TextSecondary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Modern Frosted Glass Status Badge Pill
            val pillShape = RoundedCornerShape(12.dp)
            Box(
                modifier = Modifier
                    .clip(pillShape)
                    .background(
                        if (isFocused) {
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color(0x6038BDF8),
                                    Color(0x250284C7)
                                )
                            )
                        } else {
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color(0x35FFFFFF),
                                    Color(0x10FFFFFF)
                                )
                            )
                        }
                    )
                    .border(
                        BorderStroke(
                            width = 1.dp,
                            color = if (isFocused) Color.White else Color(0x40FFFFFF)
                        ),
                        pillShape
                    )
                    .padding(horizontal = 14.dp, vertical = 6.dp)
            ) {
                Text(
                    text = badgeValue,
                    color = if (isFocused) Color.White else AccentCyan,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
            }
        }

        // Specular Sheen on Focus
        if (isFocused) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(GlassSpecularSheen)
            )
        }
    }
}

