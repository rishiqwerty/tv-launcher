package com.glasstv.launcher.ui.components

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.material3.Text
import com.glasstv.launcher.data.model.AppInfo
import com.glasstv.launcher.ui.theme.Dimens
import com.glasstv.launcher.ui.theme.GlassBorderDefault
import com.glasstv.launcher.ui.theme.GlassBorderFocused
import com.glasstv.launcher.ui.theme.GlassCardBase
import com.glasstv.launcher.ui.theme.GlassCardFocused
import com.glasstv.launcher.ui.theme.TextMuted
import com.glasstv.launcher.ui.theme.TextPrimary

@Composable
fun AppTile(
    app: AppInfo,
    onClick: (AppInfo) -> Unit,
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
        label = "tile_scale"
    )

    val shape = RoundedCornerShape(Dimens.CardCornerRadius)
    val hasBanner = app.banner != null
    val tileWidth = if (hasBanner) Dimens.BannerCardWidth else Dimens.AppTileWidth
    val tileHeight = if (hasBanner) Dimens.BannerCardHeight else Dimens.AppTileHeight

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .padding(horizontal = 8.dp, vertical = 12.dp)
            .scale(scale)
    ) {
        Box(
            modifier = Modifier
                .size(width = tileWidth, height = tileHeight)
                .shadow(
                    elevation = if (isFocused) 16.dp else 4.dp,
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
                    onClick = { onClick(app) }
                ),
            contentAlignment = Alignment.Center
        ) {
            if (app.banner != null) {
                val bitmap = remember(app.banner) { drawableToBitmap(app.banner) }
                if (bitmap != null) {
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = app.label,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            } else if (app.icon != null) {
                val bitmap = remember(app.icon) { drawableToBitmap(app.icon) }
                if (bitmap != null) {
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = app.label,
                        modifier = Modifier
                            .size(56.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Fit
                    )
                }
            } else {
                // High-contrast initial avatar fallback
                Text(
                    text = app.label.take(1).uppercase(),
                    color = TextPrimary,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // App Label below tile
        Text(
            text = app.label,
            color = if (isFocused) TextPrimary else TextMuted,
            fontSize = 13.sp,
            fontWeight = if (isFocused) FontWeight.SemiBold else FontWeight.Normal,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
            modifier = Modifier.size(width = tileWidth, height = 20.dp)
        )
    }
}

private fun drawableToBitmap(drawable: Drawable): Bitmap? {
    return try {
        if (drawable is BitmapDrawable && drawable.bitmap != null) {
            return drawable.bitmap
        }
        val width = if (drawable.intrinsicWidth > 0) drawable.intrinsicWidth else 96
        val height = if (drawable.intrinsicHeight > 0) drawable.intrinsicHeight else 96
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        bitmap
    } catch (e: Exception) {
        null
    }
}
