package com.glasstv.launcher.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.offset
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
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
    searchContent: (@Composable () -> Unit)? = null,
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
        val capsuleShape = RoundedCornerShape(26.dp)
        val segmentWidth = 118.dp
        val segmentHeight = 38.dp
        val segmentSpacing = 4.dp

        Box(
            modifier = Modifier
                .shadow(
                    elevation = 16.dp,
                    shape = capsuleShape,
                    spotColor = Color(0x60000000)
                )
                .clip(capsuleShape)
                .background(Color(0x350F172A))
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0x28FFFFFF),
                            Color(0x0CFFFFFF),
                            Color(0x04000000)
                        )
                    )
                )
                .border(
                    border = BorderStroke(1.dp, GlassBorderGradient),
                    shape = capsuleShape
                )
                .padding(4.dp)
        ) {
            // Gliding Translucent Active Indicator Pill
            val targetOffsetX = when (currentScreen) {
                LauncherScreen.HOME -> 0.dp
                LauncherScreen.ALL_APPS -> segmentWidth + segmentSpacing
                LauncherScreen.SETTINGS -> (segmentWidth + segmentSpacing) * 2
            }

            val animatedOffsetX by animateDpAsState(
                targetValue = targetOffsetX,
                animationSpec = tween(
                    durationMillis = 240,
                    easing = FastOutSlowInEasing
                ),
                label = "gliding_tab_indicator"
            )

            val indicatorShape = RoundedCornerShape(20.dp)

            Box(
                modifier = Modifier
                    .offset(x = animatedOffsetX)
                    .size(width = segmentWidth, height = segmentHeight)
                    .shadow(
                        elevation = 6.dp,
                        shape = indicatorShape,
                        spotColor = AccentCyan.copy(alpha = 0.35f)
                    )
                    .clip(indicatorShape)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0x3238BDF8),
                                Color(0x140284C7)
                            )
                        )
                    )
                    .border(
                        BorderStroke(
                            1.dp,
                            Brush.verticalGradient(
                                listOf(
                                    Color(0x90FFFFFF),
                                    Color(0x3538BDF8)
                                )
                            )
                        ),
                        indicatorShape
                    )
            )

            // Interactive Navigation Segments Row
            Row(
                horizontalArrangement = Arrangement.spacedBy(segmentSpacing),
                verticalAlignment = Alignment.CenterVertically
            ) {
                LauncherScreen.values().forEach { screen ->
                    GlassNavSegment(
                        screen = screen,
                        isSelected = currentScreen == screen,
                        width = segmentWidth,
                        height = segmentHeight,
                        onClick = { onScreenSelected(screen) }
                    )
                }
            }
        }

        // Trailing Actions: Optional Search Bar/Button & Glass Clock Pill
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            searchContent?.invoke()

            if (showClock) {
                LuxuryGlassClockPill(is24Hour = is24Hour)
            }
        }
    }
}

@Composable
private fun GlassNavSegment(
    screen: LauncherScreen,
    isSelected: Boolean,
    width: androidx.compose.ui.unit.Dp,
    height: androidx.compose.ui.unit.Dp,
    onClick: () -> Unit
) {
    var isFocused by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }

    val scale by animateFloatAsState(
        targetValue = if (isFocused) 1.05f else 1.0f,
        animationSpec = tween(
            durationMillis = 110,
            easing = FastOutSlowInEasing
        ),
        label = "nav_segment_scale"
    )

    val segmentShape = RoundedCornerShape(20.dp)

    // Lighter, translucent focus highlight
    val backgroundModifier = when {
        isFocused -> Modifier.background(
            Brush.verticalGradient(
                colors = listOf(
                    Color(0x30FFFFFF),
                    Color(0x14FFFFFF)
                )
            )
        )
        else -> Modifier.background(Color.Transparent)
    }

    val borderModifier = when {
        isFocused -> Modifier.border(
            BorderStroke(
                1.5.dp,
                Brush.linearGradient(
                    listOf(
                        Color(0xDDFFFFFF),
                        Color(0x8038BDF8),
                        Color(0xAAFFFFFF)
                    )
                )
            ),
            segmentShape
        )
        else -> Modifier
    }

    Box(
        modifier = Modifier
            .size(width = width, height = height)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .then(
                if (isFocused) {
                    Modifier.shadow(
                        elevation = 8.dp,
                        shape = segmentShape,
                        spotColor = Color(0x60FFFFFF)
                    )
                } else Modifier
            )
            .clip(segmentShape)
            .then(backgroundModifier)
            .then(borderModifier)
            .onFocusChanged { isFocused = it.isFocused }
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .onKeyEvent { keyEvent ->
                if (keyEvent.type == KeyEventType.KeyDown && (keyEvent.key == Key.DirectionCenter || keyEvent.key == Key.Enter)) {
                    onClick()
                    true
                } else {
                    false
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
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

            Spacer(modifier = Modifier.width(6.dp))

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

