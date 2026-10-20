package com.glasstv.launcher.ui.theme

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Cinematic Obsidian & Deep Space Midnight Foundations
val BackgroundDark = Color(0xFF080C16)
val BackgroundOled = Color(0xFF000000)
val BackgroundDeepMidnight = Color(0xFF04060B)
val BackgroundGradientStart = Color(0xFF0C101B)
val BackgroundGradientEnd = Color(0xFF04060A)

// Frosted Glass Surfaces (Translucent, refractive fills)
val GlassSurface = Color(0x30FFFFFF)
val GlassSurfaceSubtle = Color(0x18FFFFFF)
val GlassSurfaceLight = Color(0x42FFFFFF)
val GlassSurfaceFocused = Color(0x55FFFFFF)
val GlassSurfaceDark = Color(0x900B1120)
val GlassSurfaceCard = Color(0x351E293B)

// Frosted Glass Specular Highlights & Borders
val GlassBorderSpecularLight = Color(0x90FFFFFF)
val GlassBorderSubtle = Color(0x30FFFFFF)
val GlassBorderFocused = Color(0xFFFFFFFF)
val GlassBorderGlow = Color(0x9938BDF8)

// Apple TV & visionOS Liquid Glass Gradients
val GlassCardFillGradient = Brush.verticalGradient(
    colors = listOf(
        Color(0x3AFFFFFF),
        Color(0x18FFFFFF),
        Color(0x06000000)
    )
)

val GlassCardFocusedGradient = Brush.verticalGradient(
    colors = listOf(
        Color(0x6AFFFFFF),
        Color(0x32FFFFFF),
        Color(0x12000000)
    )
)

// High-precision specular edge bevel (bright light catch at top, soft translucent falloff at bottom)
val GlassBorderGradient = Brush.verticalGradient(
    colors = listOf(
        Color(0x95FFFFFF),
        Color(0x35FFFFFF),
        Color(0x10FFFFFF)
    )
)

val GlassBorderFocusedGradient = Brush.linearGradient(
    colors = listOf(
        Color(0xFFFFFFFF),
        Color(0xE6FFFFFF),
        Color(0x9938BDF8),
        Color(0xFFFFFFFF)
    ),
    start = Offset(0f, 0f),
    end = Offset(400f, 400f)
)

val GlassSpecularSheen = Brush.verticalGradient(
    colors = listOf(
        Color(0x55FFFFFF),
        Color(0x20FFFFFF),
        Color(0x00FFFFFF),
        Color(0x25000000)
    )
)

// Vibrant, Atmospheric Ambient Glow Orbs for Authentic Glass Refraction
val OrbSapphire = Color(0x662563EB)
val OrbViolet = Color(0x597C3AED)
val OrbCyan = Color(0x5206B6D4)
val OrbEmerald = Color(0x4810B981)
val OrbRose = Color(0x44F43F5E)

// Typography & Accents
val TextPrimary = Color(0xFFFFFFFF)
val TextSecondary = Color(0xFFD1D5DB)
val TextMuted = Color(0xFF94A3B8)

val AccentCyan = Color(0xFF38BDF8)
val AccentBlue = Color(0xFF60A5FA)
val AccentPurple = Color(0xFFC084FC)
val AccentAmber = Color(0xFFFBBF24)
val AccentEmerald = Color(0xFF34D399)
