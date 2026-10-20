package com.glasstv.launcher.ui.components

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Applies a cinematic alpha fading edge on any scrollable container (LazyRow, LazyGrid, etc.).
 * Solves hard pixel cuts by smoothly dissolving items into the atmospheric frosted background.
 */
fun Modifier.fadingEdge(
    top: Dp = 0.dp,
    bottom: Dp = 0.dp,
    left: Dp = 0.dp,
    right: Dp = 0.dp
): Modifier = this
    .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
    .drawWithContent {
        drawContent()

        val topPx = top.toPx()
        if (topPx > 0f) {
            drawRect(
                brush = Brush.verticalGradient(
                    colors = listOf(Color.Transparent, Color.White),
                    startY = 0f,
                    endY = topPx
                ),
                topLeft = Offset(0f, 0f),
                size = Size(size.width, topPx),
                blendMode = BlendMode.DstIn
            )
        }

        val bottomPx = bottom.toPx()
        if (bottomPx > 0f) {
            drawRect(
                brush = Brush.verticalGradient(
                    colors = listOf(Color.White, Color.Transparent),
                    startY = size.height - bottomPx,
                    endY = size.height
                ),
                topLeft = Offset(0f, size.height - bottomPx),
                size = Size(size.width, bottomPx),
                blendMode = BlendMode.DstIn
            )
        }

        val leftPx = left.toPx()
        if (leftPx > 0f) {
            drawRect(
                brush = Brush.horizontalGradient(
                    colors = listOf(Color.Transparent, Color.White),
                    startX = 0f,
                    endX = leftPx
                ),
                topLeft = Offset(0f, 0f),
                size = Size(leftPx, size.height),
                blendMode = BlendMode.DstIn
            )
        }

        val rightPx = right.toPx()
        if (rightPx > 0f) {
            drawRect(
                brush = Brush.horizontalGradient(
                    colors = listOf(Color.White, Color.Transparent),
                    startX = size.width - rightPx,
                    endX = size.width
                ),
                topLeft = Offset(size.width - rightPx, 0f),
                size = Size(rightPx, size.height),
                blendMode = BlendMode.DstIn
            )
        }
    }

