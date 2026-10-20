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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.tv.material3.Icon
import androidx.tv.material3.Text
import com.glasstv.launcher.data.model.AppInfo
import com.glasstv.launcher.ui.theme.AccentCyan
import com.glasstv.launcher.ui.theme.Dimens
import com.glasstv.launcher.ui.theme.GlassBorderFocusedGradient
import com.glasstv.launcher.ui.theme.GlassBorderGradient
import com.glasstv.launcher.ui.theme.GlassCardFillGradient
import com.glasstv.launcher.ui.theme.GlassCardFocusedGradient
import com.glasstv.launcher.ui.theme.TextMuted
import com.glasstv.launcher.ui.theme.TextPrimary
import com.glasstv.launcher.ui.theme.TextSecondary

@Composable
fun AppActionDialog(
    app: AppInfo,
    onDismiss: () -> Unit,
    onOpen: (AppInfo) -> Unit,
    onToggleFavorite: (AppInfo) -> Unit,
    onMoveLeft: ((AppInfo) -> Unit)? = null,
    onMoveRight: ((AppInfo) -> Unit)? = null,
    onAppInfo: ((AppInfo) -> Unit)? = null
) {
    val firstFocusRequester = remember { FocusRequester() }

    androidx.compose.runtime.LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(100)
        try {
            firstFocusRequester.requestFocus()
        } catch (e: Exception) {
            // Ignore focus exception
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        val shape = RoundedCornerShape(24.dp)

        Box(
            modifier = Modifier
                .width(440.dp)
                .shadow(24.dp, shape, spotColor = AccentCyan)
                .clip(shape)
                .background(Color(0xE80A0E1A))
                .background(GlassCardFillGradient)
                .border(BorderStroke(1.5.dp, GlassBorderFocusedGradient), shape)
                .padding(28.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                // App Title
                Text(
                    text = app.label,
                    color = TextPrimary,
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.3.sp
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = app.packageName,
                    color = TextMuted,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Normal
                )

                Spacer(modifier = Modifier.height(22.dp))

                // Action 1: Open
                DialogActionButton(
                    text = "Open Application",
                    icon = Icons.Default.PlayArrow,
                    modifier = Modifier.focusRequester(firstFocusRequester),
                    onClick = {
                        onDismiss()
                        onOpen(app)
                    }
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Action 2: Favorite / Unfavorite
                DialogActionButton(
                    text = if (app.isFavorite) "Remove from Favorites" else "Add to Favorites",
                    icon = if (app.isFavorite) Icons.Default.StarBorder else Icons.Default.Star,
                    highlight = true,
                    onClick = {
                        onDismiss()
                        onToggleFavorite(app)
                    }
                )

                if (app.isFavorite && onMoveLeft != null) {
                    Spacer(modifier = Modifier.height(10.dp))
                    DialogActionButton(
                        text = "Move Left",
                        icon = Icons.Default.ArrowBack,
                        onClick = {
                            onDismiss()
                            onMoveLeft(app)
                        }
                    )
                }

                if (app.isFavorite && onMoveRight != null) {
                    Spacer(modifier = Modifier.height(10.dp))
                    DialogActionButton(
                        text = "Move Right",
                        icon = Icons.Default.ArrowForward,
                        onClick = {
                            onDismiss()
                            onMoveRight(app)
                        }
                    )
                }

                if (onAppInfo != null) {
                    Spacer(modifier = Modifier.height(10.dp))
                    DialogActionButton(
                        text = "App Details",
                        icon = Icons.Default.Info,
                        onClick = {
                            onDismiss()
                            onAppInfo(app)
                        }
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Dismiss
                DialogActionButton(
                    text = "Dismiss",
                    icon = Icons.Default.Close,
                    onClick = onDismiss
                )
            }
        }
    }
}

@Composable
private fun DialogActionButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    highlight: Boolean = false
) {
    var isFocused by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }

    val scale by animateFloatAsState(
        targetValue = if (isFocused) 1.04f else 1.0f,
        animationSpec = spring(dampingRatio = 0.8f, stiffness = 400f),
        label = "dialog_button_scale"
    )

    val shape = RoundedCornerShape(14.dp)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .scale(scale)
            .shadow(
                elevation = if (isFocused) 10.dp else 0.dp,
                shape = shape,
                spotColor = if (highlight) AccentCyan else Color.White
            )
            .clip(shape)
            .background(Color(0x350A0E18))
            .background(
                if (isFocused) {
                    GlassCardFocusedGradient
                } else if (highlight) {
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0x3838BDF8),
                            Color(0x1538BDF8)
                        )
                    )
                } else {
                    GlassCardFillGradient
                }
            )
            .border(
                border = BorderStroke(
                    width = if (isFocused) 2.dp else 1.dp,
                    brush = if (isFocused) {
                        GlassBorderFocusedGradient
                    } else if (highlight) {
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0xAA38BDF8),
                                Color(0x3038BDF8)
                            )
                        )
                    } else {
                        GlassBorderGradient
                    }
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
            .padding(horizontal = 18.dp, vertical = 13.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = when {
                        isFocused -> TextPrimary
                        highlight -> AccentCyan
                        else -> TextSecondary
                    },
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
            }
            Text(
                text = text,
                color = when {
                    isFocused -> TextPrimary
                    highlight -> AccentCyan
                    else -> TextPrimary.copy(alpha = 0.95f)
                },
                fontSize = 14.sp,
                fontWeight = if (isFocused || highlight) FontWeight.Bold else FontWeight.Medium
            )
        }
    }
}
