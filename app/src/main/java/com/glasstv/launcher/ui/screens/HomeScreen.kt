package com.glasstv.launcher.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.foundation.lazy.list.TvLazyRow
import androidx.tv.foundation.lazy.list.items
import androidx.tv.material3.Text
import com.glasstv.launcher.data.model.AppInfo
import com.glasstv.launcher.data.model.SystemShortcut
import com.glasstv.launcher.data.model.ThemeMode
import com.glasstv.launcher.ui.components.AppTile
import com.glasstv.launcher.ui.components.ClockWidget
import com.glasstv.launcher.ui.components.ShortcutTile
import com.glasstv.launcher.ui.theme.AccentCyan
import com.glasstv.launcher.ui.theme.BackgroundDark
import com.glasstv.launcher.ui.theme.BackgroundGradientEnd
import com.glasstv.launcher.ui.theme.BackgroundGradientStart
import com.glasstv.launcher.ui.theme.BackgroundOled
import com.glasstv.launcher.ui.theme.Dimens
import com.glasstv.launcher.ui.theme.TextMuted
import com.glasstv.launcher.ui.theme.TextPrimary
import com.glasstv.launcher.ui.viewmodel.HomeUiState

@Composable
fun HomeScreen(
    uiState: HomeUiState,
    onAppClick: (AppInfo) -> Unit,
    onShortcutClick: (SystemShortcut) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val backgroundModifier = when (uiState.settings.themeMode) {
        ThemeMode.OLED_BLACK -> Modifier.background(BackgroundOled)
        ThemeMode.SUBTLE_GRADIENT -> Modifier.background(
            Brush.verticalGradient(
                colors = listOf(BackgroundGradientStart, BackgroundGradientEnd)
            )
        )
        ThemeMode.DARK -> Modifier.background(
            Brush.verticalGradient(
                colors = listOf(BackgroundDark, Color(0xFF04060A))
            )
        )
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .then(backgroundModifier)
            .padding(
                horizontal = Dimens.ScreenHorizontalPadding,
                vertical = Dimens.ScreenVerticalPadding
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // 1. Top Status Bar: Title & Live Clock
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Dimens.TopBarHeight),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Glass",
                        color = AccentCyan,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.padding(start = 4.dp))
                    Text(
                        text = "TV",
                        color = TextPrimary,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Light,
                        letterSpacing = 1.sp
                    )
                }

                if (uiState.settings.showClock) {
                    ClockWidget(is24Hour = uiState.settings.clockFormat24Hour)
                }
            }

            Spacer(modifier = Modifier.height(Dimens.SectionSpacing))

            // 2. Favorites Apps Row
            Text(
                text = "FAVORITES",
                color = TextMuted,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 1.5.sp,
                modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
            )

            if (uiState.favoriteApps.isEmpty() && !uiState.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No TV applications installed",
                        color = TextMuted,
                        fontSize = 14.sp
                    )
                }
            } else {
                TvLazyRow(
                    contentPadding = PaddingValues(horizontal = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(
                        items = uiState.favoriteApps,
                        key = { it.key }
                    ) { app ->
                        AppTile(
                            app = app,
                            onClick = onAppClick,
                            isReducedMotion = uiState.settings.reducedMotion
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(Dimens.SectionSpacing))

            // 3. Quick System Shortcuts Row
            Text(
                text = "QUICK ACCESS",
                color = TextMuted,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 1.5.sp,
                modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
            )

            TvLazyRow(
                contentPadding = PaddingValues(horizontal = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    items = uiState.shortcuts,
                    key = { it.id }
                ) { shortcut ->
                    ShortcutTile(
                        shortcut = shortcut,
                        onClick = onShortcutClick,
                        isReducedMotion = uiState.settings.reducedMotion
                    )
                }
            }
        }
    }
}
