package com.glasstv.launcher.ui.screens

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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.foundation.lazy.list.TvLazyRow
import androidx.tv.foundation.lazy.list.items
import androidx.tv.foundation.lazy.list.itemsIndexed
import androidx.tv.material3.Icon
import androidx.tv.material3.Text
import com.glasstv.launcher.data.model.AppInfo
import com.glasstv.launcher.data.model.SystemShortcut
import com.glasstv.launcher.ui.components.AppTile
import com.glasstv.launcher.ui.components.GlassBackdrop
import com.glasstv.launcher.ui.components.LauncherScreen
import com.glasstv.launcher.ui.components.ShortcutTile
import com.glasstv.launcher.ui.components.TopNavBar
import com.glasstv.launcher.ui.components.fadingEdge
import com.glasstv.launcher.ui.theme.AccentCyan
import com.glasstv.launcher.ui.theme.AccentPurple
import com.glasstv.launcher.ui.theme.Dimens
import com.glasstv.launcher.ui.theme.GlassBorderFocusedGradient
import com.glasstv.launcher.ui.theme.GlassBorderGradient
import com.glasstv.launcher.ui.theme.GlassCardFillGradient
import com.glasstv.launcher.ui.theme.GlassCardFocusedGradient
import com.glasstv.launcher.ui.theme.TextMuted
import com.glasstv.launcher.ui.theme.TextPrimary
import com.glasstv.launcher.ui.theme.TextSecondary
import com.glasstv.launcher.ui.viewmodel.HomeUiState
import kotlinx.coroutines.delay

@Composable
fun HomeScreen(
    uiState: HomeUiState,
    onAppClick: (AppInfo) -> Unit,
    onAppLongClick: (AppInfo) -> Unit,
    onShortcutClick: (SystemShortcut) -> Unit,
    onScreenSelected: (LauncherScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val firstFocusRequester = remember { FocusRequester() }

    LaunchedEffect(uiState.favoriteApps.size, uiState.isLoading) {
        if (!uiState.isLoading) {
            delay(100)
            try {
                firstFocusRequester.requestFocus()
            } catch (e: Exception) {
                // Ignore focus exception
            }
        }
    }

    GlassBackdrop(
        settings = uiState.settings,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = Dimens.ScreenVerticalPadding)
                .verticalScroll(rememberScrollState())
        ) {
            // 1. Top Navigation Bar: Floating Frosted Capsule & Live Clock
            TopNavBar(
                currentScreen = LauncherScreen.HOME,
                onScreenSelected = onScreenSelected,
                showClock = uiState.settings.showClock,
                is24Hour = uiState.settings.clockFormat24Hour,
                modifier = Modifier.padding(horizontal = 48.dp)
            )

            Spacer(modifier = Modifier.height(Dimens.SectionSpacing))

            // 2. Favorites Apps Row Header
            SectionHeader(
                title = "FAVORITES",
                dotColor = AccentCyan,
                badgeText = if (uiState.favoriteApps.isNotEmpty()) "${uiState.favoriteApps.size} APPS" else null,
                modifier = Modifier.padding(start = 48.dp, bottom = 12.dp)
            )

            if (uiState.favoriteApps.isEmpty() && !uiState.isLoading) {
                // Luxury Frosted Glass Empty State Card
                val emptyShape = RoundedCornerShape(20.dp)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 48.dp, vertical = 8.dp)
                        .shadow(12.dp, emptyShape, spotColor = Color(0x50000000))
                        .clip(emptyShape)
                        .background(Color(0x30121927))
                        .background(GlassCardFillGradient)
                        .border(BorderStroke(1.dp, GlassBorderGradient), emptyShape)
                        .padding(horizontal = 28.dp, vertical = 26.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(52.dp)
                                .clip(CircleShape)
                                .background(Color(0x3038BDF8))
                                .border(BorderStroke(1.dp, Color(0x6038BDF8)), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.StarBorder,
                                contentDescription = null,
                                tint = AccentCyan,
                                modifier = Modifier.size(28.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "No Favorite Applications Pinned",
                            color = TextPrimary,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Browse your TV apps and press the Menu button (or hold Select) on any app to pin it here.",
                            color = TextSecondary,
                            fontSize = 13.sp,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(18.dp))

                        // Browse All Apps Button
                        var isBtnFocused by remember { mutableStateOf(false) }
                        val btnInteraction = remember { MutableInteractionSource() }
                        val btnScale by animateFloatAsState(
                            targetValue = if (isBtnFocused) 1.05f else 1.0f,
                            animationSpec = spring(dampingRatio = 0.8f, stiffness = 400f),
                            label = "empty_btn_scale"
                        )

                        val btnShape = RoundedCornerShape(14.dp)
                        Box(
                            modifier = Modifier
                                .scale(btnScale)
                                .focusRequester(firstFocusRequester)
                                .shadow(if (isBtnFocused) 12.dp else 0.dp, btnShape, spotColor = AccentCyan)
                                .clip(btnShape)
                                .background(if (isBtnFocused) GlassCardFocusedGradient else GlassCardFillGradient)
                                .border(
                                    BorderStroke(
                                        width = if (isBtnFocused) 2.dp else 1.dp,
                                        brush = if (isBtnFocused) GlassBorderFocusedGradient else GlassBorderGradient
                                    ),
                                    btnShape
                                )
                                .onFocusChanged { isBtnFocused = it.isFocused }
                                .focusable(interactionSource = btnInteraction)
                                .clickable(
                                    interactionSource = btnInteraction,
                                    indication = null,
                                    onClick = { onScreenSelected(LauncherScreen.ALL_APPS) }
                                )
                                .padding(horizontal = 26.dp, vertical = 12.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Apps,
                                    contentDescription = null,
                                    tint = if (isBtnFocused) TextPrimary else AccentCyan,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = "Browse All Apps",
                                    color = if (isBtnFocused) TextPrimary else AccentCyan,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            } else {
                TvLazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fadingEdge(left = 48.dp, right = 48.dp),
                    contentPadding = PaddingValues(start = 48.dp, end = 48.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    itemsIndexed(
                        items = uiState.favoriteApps,
                        key = { _, app -> app.key }
                    ) { index, app ->
                        AppTile(
                            app = app,
                            onClick = onAppClick,
                            onLongClick = onAppLongClick,
                            isReducedMotion = uiState.settings.reducedMotion,
                            modifier = if (index == 0) Modifier.focusRequester(firstFocusRequester) else Modifier
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(Dimens.SectionSpacing))

            // 3. Quick System Shortcuts Row
            SectionHeader(
                title = "QUICK ACCESS",
                dotColor = AccentPurple,
                badgeText = "CONTROLS",
                modifier = Modifier.padding(start = 48.dp, bottom = 12.dp)
            )

            TvLazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .fadingEdge(left = 48.dp, right = 48.dp),
                contentPadding = PaddingValues(start = 48.dp, end = 48.dp),
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

@Composable
private fun SectionHeader(
    title: String,
    dotColor: Color,
    badgeText: String? = null,
    modifier: Modifier = Modifier.padding(start = 48.dp, bottom = 12.dp)
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
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
        if (badgeText != null) {
            Spacer(modifier = Modifier.width(10.dp))
            val badgeShape = RoundedCornerShape(8.dp)
            Box(
                modifier = Modifier
                    .clip(badgeShape)
                    .background(Color(0x25FFFFFF))
                    .border(BorderStroke(1.dp, Color(0x35FFFFFF)), badgeShape)
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
                Text(
                    text = badgeText,
                    color = AccentCyan,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
            }
        }
    }
}

