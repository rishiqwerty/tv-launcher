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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.foundation.lazy.grid.TvGridCells
import androidx.tv.foundation.lazy.grid.TvLazyVerticalGrid
import androidx.tv.material3.Icon
import androidx.tv.material3.Text
import com.glasstv.launcher.data.model.AppInfo
import com.glasstv.launcher.ui.components.AppTile
import com.glasstv.launcher.ui.components.GlassBackdrop
import com.glasstv.launcher.ui.components.LauncherScreen
import com.glasstv.launcher.ui.components.TopNavBar
import com.glasstv.launcher.ui.theme.AccentCyan
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
fun AllAppsScreen(
    uiState: HomeUiState,
    onAppClick: (AppInfo) -> Unit,
    onAppLongClick: ((AppInfo) -> Unit)? = null,
    onScreenSelected: (LauncherScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    val firstFocusRequester = remember { FocusRequester() }

    LaunchedEffect(uiState.allApps.size) {
        if (uiState.allApps.isNotEmpty()) {
            delay(100)
            try {
                firstFocusRequester.requestFocus()
            } catch (e: Exception) {
            }
        }
    }

    val filteredApps = remember(searchQuery, uiState.allApps) {
        if (searchQuery.isBlank()) {
            uiState.allApps
        } else {
            uiState.allApps.filter { it.label.contains(searchQuery.trim(), ignoreCase = true) }
        }
    }

    GlassBackdrop(
        settings = uiState.settings,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    horizontal = Dimens.ScreenHorizontalPadding,
                    vertical = Dimens.ScreenVerticalPadding
                )
        ) {
            // Top Navigation Bar
            TopNavBar(
                currentScreen = LauncherScreen.ALL_APPS,
                onScreenSelected = onScreenSelected,
                showClock = uiState.settings.showClock,
                is24Hour = uiState.settings.clockFormat24Hour
            )

            Spacer(modifier = Modifier.height(Dimens.SectionSpacing))

            // Section Header & Search Bar Row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 8.dp, bottom = 16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(width = 4.dp, height = 15.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(AccentCyan)
                            .shadow(6.dp, RoundedCornerShape(2.dp), spotColor = AccentCyan)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "ALL APPLICATIONS",
                        color = TextPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp
                    )
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
                            text = if (searchQuery.isBlank()) "${uiState.allApps.size} APPS" else "${filteredApps.size} OF ${uiState.allApps.size}",
                            color = AccentCyan,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        )
                    }
                }

                // Remote-friendly Frosted Glass Search Bar Pill
                AppSearchBar(
                    query = searchQuery,
                    onQueryChange = { searchQuery = it },
                    onClear = { searchQuery = "" }
                )
            }

            if (filteredApps.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (uiState.isLoading) {
                            "Loading applications..."
                        } else if (searchQuery.isNotBlank()) {
                            "No applications matching \"$searchQuery\""
                        } else {
                            "No applications found"
                        },
                        color = TextMuted,
                        fontSize = 14.sp
                    )
                }
            } else {
                TvLazyVerticalGrid(
                    columns = TvGridCells.Adaptive(minSize = 180.dp),
                    contentPadding = PaddingValues(bottom = 32.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(
                        count = filteredApps.size,
                        key = { index -> filteredApps[index].key }
                    ) { index ->
                        val app = filteredApps[index]
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
        }
    }
}

@Composable
private fun AppSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onClear: () -> Unit
) {
    var isFocused by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }

    val scale by animateFloatAsState(
        targetValue = if (isFocused) 1.05f else 1.0f,
        animationSpec = spring(dampingRatio = 0.8f, stiffness = 400f),
        label = "search_scale"
    )

    val shape = RoundedCornerShape(22.dp)

    Box(
        modifier = Modifier
            .width(280.dp)
            .scale(scale)
            .shadow(
                elevation = if (isFocused) 14.dp else 4.dp,
                shape = shape,
                spotColor = if (isFocused) AccentCyan else Color(0x60000000)
            )
            .clip(shape)
            .background(Color(0x350F172A))
            .background(
                Brush.verticalGradient(
                    colors = if (isFocused) {
                        listOf(
                            Color(0x65FFFFFF),
                            Color(0x30FFFFFF),
                            Color(0x10000000)
                        )
                    } else {
                        listOf(
                            Color(0x30FFFFFF),
                            Color(0x12FFFFFF),
                            Color(0x04000000)
                        )
                    }
                )
            )
            .border(
                border = BorderStroke(
                    width = if (isFocused) 2.dp else 1.dp,
                    brush = if (isFocused) GlassBorderFocusedGradient else GlassBorderGradient
                ),
                shape = shape
            )
            .onFocusChanged { isFocused = it.isFocused }
            .padding(horizontal = 16.dp, vertical = 9.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = if (isFocused) AccentCyan else TextSecondary,
                modifier = Modifier.size(17.dp)
            )

            Spacer(modifier = Modifier.width(10.dp))

            BasicTextField(
                value = query,
                onValueChange = onQueryChange,
                singleLine = true,
                cursorBrush = SolidColor(AccentCyan),
                textStyle = TextStyle(
                    color = TextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                ),
                modifier = Modifier
                    .weight(1f)
                    .focusable(interactionSource = interactionSource),
                decorationBox = { innerTextField ->
                    if (query.isEmpty()) {
                        Text(
                            text = "Search apps...",
                            color = TextMuted,
                            fontSize = 13.sp
                        )
                    }
                    innerTextField()
                }
            )

            if (query.isNotEmpty()) {
                Spacer(modifier = Modifier.width(6.dp))
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = "Clear",
                    tint = TextSecondary,
                    modifier = Modifier
                        .size(16.dp)
                        .clickable { onClear() }
                )
            }
        }
    }
}

