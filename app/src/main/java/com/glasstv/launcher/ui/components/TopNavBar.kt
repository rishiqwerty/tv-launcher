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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.material3.Icon
import androidx.tv.material3.Text
import com.glasstv.launcher.ui.theme.AccentCyan
import com.glasstv.launcher.ui.theme.AccentEmerald
import com.glasstv.launcher.ui.theme.Dimens
import com.glasstv.launcher.ui.theme.GlassBorderFocusedGradient
import com.glasstv.launcher.ui.theme.GlassBorderGradient
import com.glasstv.launcher.ui.theme.TextPrimary
import com.glasstv.launcher.ui.theme.TextSecondary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class LauncherScreen(val title: String, val icon: ImageVector) {
    HOME("Home", Icons.Default.Home),
    ALL_APPS("All Apps", Icons.Default.Apps),
    SETTINGS("Settings", Icons.Default.Settings)
}

@Composable
fun TopNavBar(
    currentScreen: LauncherScreen,
    onScreenSelected: (LauncherScreen) -> Unit,
    showClock: Boolean = true,
    is24Hour: Boolean = false,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(Dimens.TopBarHeight),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Floating Frosted Glass Segmented Control Island
        val capsuleShape = RoundedCornerShape(28.dp)
        Box(
            modifier = Modifier
                .shadow(
                    elevation = 16.dp,
                    shape = capsuleShape,
                    spotColor = Color(0x80000000)
                )
                .clip(capsuleShape)
                .background(Color(0x400F172A))
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0x38FFFFFF),
                            Color(0x14FFFFFF),
                            Color(0x06000000)
                        )
                    )
                )
                .border(
                    border = BorderStroke(1.dp, GlassBorderGradient),
                    shape = capsuleShape
                )
                .padding(4.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                LauncherScreen.values().forEach { screen ->
                    GlassNavSegment(
                        screen = screen,
                        isSelected = currentScreen == screen,
                        onClick = { onScreenSelected(screen) }
                    )
                }
            }
        }

        // Luxury Floating Frosted Glass Clock Pill
        if (showClock) {
            LuxuryGlassClockPill(is24Hour = is24Hour)
        }
    }
}

@Composable
private fun GlassNavSegment(
    screen: LauncherScreen,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    var isFocused by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }

    val scale by animateFloatAsState(
        targetValue = if (isFocused) 1.06f else 1.0f,
        animationSpec = spring(dampingRatio = 0.8f, stiffness = 400f),
        label = "nav_segment_scale"
    )

    val segmentShape = RoundedCornerShape(22.dp)

    val backgroundModifier = when {
        isFocused -> Modifier.background(
            Brush.verticalGradient(
                colors = listOf(
                    Color(0x75FFFFFF),
                    Color(0x35FFFFFF)
                )
            )
        )
        isSelected -> Modifier.background(
            Brush.verticalGradient(
                colors = listOf(
                    Color(0x5538BDF8),
                    Color(0x220284C7)
                )
            )
        )
        else -> Modifier.background(Color.Transparent)
    }

    val borderModifier = when {
        isFocused -> Modifier.border(
            BorderStroke(2.dp, GlassBorderFocusedGradient),
            segmentShape
        )
        isSelected -> Modifier.border(
            BorderStroke(
                1.dp,
                Brush.verticalGradient(
                    listOf(
                        Color(0xB3FFFFFF),
                        Color(0x6038BDF8)
                    )
                )
            ),
            segmentShape
        )
        else -> Modifier
    }

    Box(
        modifier = Modifier
            .scale(scale)
            .shadow(
                elevation = if (isFocused) 12.dp else if (isSelected) 6.dp else 0.dp,
                shape = segmentShape,
                spotColor = if (isFocused) Color.White else AccentCyan
            )
            .clip(segmentShape)
            .then(backgroundModifier)
            .then(borderModifier)
            .onFocusChanged { isFocused = it.isFocused }
            .focusable(interactionSource = interactionSource)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .padding(horizontal = 18.dp, vertical = 9.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = screen.icon,
                contentDescription = screen.title,
                tint = when {
                    isFocused -> TextPrimary
                    isSelected -> Color.White
                    else -> Color(0xB3CBD5E1)
                },
                modifier = Modifier.size(17.dp)
            )

            Text(
                text = screen.title,
                color = when {
                    isFocused -> TextPrimary
                    isSelected -> Color.White
                    else -> TextSecondary
                },
                fontSize = 13.sp,
                fontWeight = if (isFocused || isSelected) FontWeight.Bold else FontWeight.SemiBold,
                letterSpacing = 0.3.sp
            )
        }
    }
}

@Composable
private fun LuxuryGlassClockPill(is24Hour: Boolean) {
    var currentTime by remember { mutableStateOf("") }
    var currentDate by remember { mutableStateOf("") }

    androidx.compose.runtime.LaunchedEffect(is24Hour) {
        while (true) {
            val now = Date()
            val timePattern = if (is24Hour) "HH:mm" else "h:mm a"
            currentTime = SimpleDateFormat(timePattern, Locale.getDefault()).format(now)
            currentDate = SimpleDateFormat("EEE, MMM d", Locale.getDefault()).format(now)
            kotlinx.coroutines.delay(1000)
        }
    }

    val shape = RoundedCornerShape(24.dp)

    Box(
        modifier = Modifier
            .shadow(
                elevation = 14.dp,
                shape = shape,
                spotColor = Color(0x70000000)
            )
            .clip(shape)
            .background(Color(0x400F172A))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0x35FFFFFF),
                        Color(0x12FFFFFF),
                        Color(0x04000000)
                    )
                )
            )
            .border(
                border = BorderStroke(1.dp, GlassBorderGradient),
                shape = shape
            )
            .padding(horizontal = 18.dp, vertical = 9.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Live Status Indicator Pulse Beacon
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(AccentEmerald)
                    .shadow(6.dp, CircleShape, spotColor = AccentEmerald)
            )

            // Current Time (Bold Glass White)
            Text(
                text = currentTime,
                color = TextPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.4.sp
            )

            // Vertical Glass Divider Hairline
            Box(
                modifier = Modifier
                    .size(width = 1.dp, height = 13.dp)
                    .background(Color(0x40FFFFFF))
            )

            // Current Date
            Text(
                text = currentDate,
                color = TextSecondary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

