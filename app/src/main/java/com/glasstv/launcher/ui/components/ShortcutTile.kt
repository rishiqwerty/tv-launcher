package com.glasstv.launcher.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.material3.Text
import com.glasstv.launcher.data.model.SystemShortcut
import com.glasstv.launcher.ui.theme.Dimens
import com.glasstv.launcher.ui.theme.GlassBorderDefault
import com.glasstv.launcher.ui.theme.GlassBorderFocused
import com.glasstv.launcher.ui.theme.GlassCardBase
import com.glasstv.launcher.ui.theme.GlassCardFocused
import com.glasstv.launcher.ui.theme.TextMuted
import com.glasstv.launcher.ui.theme.TextPrimary

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
        animationSpec = spring(dampingRatio = 0.8f, stiffness = 400f),
        label = "shortcut_scale"
    )

    val shape = RoundedCornerShape(12.dp)

    Box(
        modifier = modifier
            .padding(horizontal = 6.dp, vertical = 8.dp)
            .scale(scale)
            .size(width = Dimens.ShortcutCardWidth, height = Dimens.ShortcutCardHeight)
            .shadow(
                elevation = if (isFocused) 12.dp else 2.dp,
                shape = shape,
                spotColor = if (isFocused) Color.White else Color.Black
            )
            .clip(shape)
            .background(if (isFocused) GlassCardFocused else GlassCardBase)
            .border(
                border = BorderStroke(
                    width = if (isFocused) Dimens.FocusBorderWidth else Dimens.DefaultBorderWidth,
                    color = if (isFocused) GlassBorderFocused else GlassBorderDefault
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
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Text(
                text = shortcut.title,
                color = if (isFocused) TextPrimary else TextMuted,
                fontSize = 14.sp,
                fontWeight = if (isFocused) FontWeight.SemiBold else FontWeight.Medium
            )
        }
    }
}
