package com.glasstv.launcher.ui.screens

import androidx.activity.compose.BackHandler
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
import androidx.compose.material.icons.filled.Close
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
import com.glasstv.launcher.ui.components.fadingEdge
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
    var isSearchExpanded by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    val firstFocusRequester = remember { FocusRequester() }
    val searchInputFocusRequester = remember { FocusRequester() }
    val searchButtonFocusRequester = remember { FocusRequester() }

    LaunchedEffect(uiState.allApps.size) {
        if (uiState.allApps.isNotEmpty() && !isSearchExpanded) {
            delay(100)
            try {
                firstFocusRequester.requestFocus()
            } catch (e: Exception) {
            }
        }
    }

    LaunchedEffect(isSearchExpanded) {
        if (isSearchExpanded) {
            delay(80)
            try {
                searchInputFocusRequester.requestFocus()
            } catch (e: Exception) {
            }
        }
    }

    // Remote Back button dismisses search if expanded
    BackHandler(enabled = isSearchExpanded) {
        if (searchQuery.isNotEmpty()) {
            searchQuery = ""
        } else {
            isSearchExpanded = false
            try {
                searchButtonFocusRequester.requestFocus()
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
                .padding(vertical = Dimens.ScreenVerticalPadding)
        ) {
            // Top Navigation Bar with Interactive Search Action
            TopNavBar(
                currentScreen = LauncherScreen.ALL_APPS,
                onScreenSelected = onScreenSelected,
                showClock = uiState.settings.showClock,
                is24Hour = uiState.settings.clockFormat24Hour,
                searchContent = {
                    if (isSearchExpanded) {
                        TopSearchBarExpanded(
                            query = searchQuery,
                            onQueryChange = { searchQuery = it },
                            onClose = {
                                isSearchExpanded = false
                                searchQuery = ""
                                try {
                                    searchButtonFocusRequester.requestFocus()
                                } catch (e: Exception) {
                                }
                            },
                            focusRequester = searchInputFocusRequester
                        )
                    } else {
                        TopSearchActionButton(
                            onClick = { isSearchExpanded = true },
                            modifier = Modifier.focusRequester(searchButtonFocusRequester)
                        )
                    }
                },
                modifier = Modifier.padding(horizontal = 48.dp)
            )

            Spacer(modifier = Modifier.height(14.dp))

            if (filteredApps.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
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

                        if (searchQuery.isNotBlank()) {
                            Spacer(modifier = Modifier.height(16.dp))
                            var isClearFocused by remember { mutableStateOf(false) }
                            val clearInteraction = remember { MutableInteractionSource() }
                            val clearShape = RoundedCornerShape(14.dp)
                            Box(
                                modifier = Modifier
                                    .clip(clearShape)
                                    .background(if (isClearFocused) GlassCardFocusedGradient else GlassCardFillGradient)
                                    .border(
                                        BorderStroke(
                                            width = if (isClearFocused) 2.dp else 1.dp,
                                            brush = if (isClearFocused) GlassBorderFocusedGradient else GlassBorderGradient
                                        ),
                                        clearShape
                                    )
                                    .onFocusChanged { isClearFocused = it.isFocused }
                                    .focusable(interactionSource = clearInteraction)
                                    .clickable(
                                        interactionSource = clearInteraction,
                                        indication = null,
                                        onClick = {
                                            searchQuery = ""
                                            isSearchExpanded = false
                                        }
                                    )
                                    .padding(horizontal = 20.dp, vertical = 10.dp)
                            ) {
                                Text(
                                    text = "Clear Search",
                                    color = if (isClearFocused) TextPrimary else AccentCyan,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            } else {
                // Expanded Apps Grid with Pure Optical Dissolve Fading Edge
                TvLazyVerticalGrid(
                    columns = TvGridCells.Adaptive(minSize = 164.dp),
                    contentPadding = PaddingValues(start = 48.dp, end = 48.dp, top = 8.dp, bottom = 48.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .fadingEdge(top = 28.dp, bottom = 48.dp, left = 48.dp, right = 48.dp)
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
                            modifier = if (index == 0 && !isSearchExpanded) Modifier.focusRequester(firstFocusRequester) else Modifier
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TopSearchActionButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isFocused by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }

    val scale by animateFloatAsState(
        targetValue = if (isFocused) 1.08f else 1.0f,
        animationSpec = spring(dampingRatio = 0.8f, stiffness = 400f),
        label = "search_btn_scale"
    )

    val shape = RoundedCornerShape(22.dp)

    Box(
        modifier = modifier
            .scale(scale)
            .shadow(
                elevation = if (isFocused) 14.dp else 4.dp,
                shape = shape,
                spotColor = if (isFocused) AccentCyan else Color(0x60000000)
            )
            .clip(shape)
            .background(Color(0x400F172A))
            .background(
                Brush.verticalGradient(
                    colors = if (isFocused) {
                        listOf(
                            Color(0x75FFFFFF),
                            Color(0x35FFFFFF)
                        )
                    } else {
                        listOf(
                            Color(0x35FFFFFF),
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
            .focusable(interactionSource = interactionSource)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .padding(horizontal = 14.dp, vertical = 9.dp),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "Search",
            tint = if (isFocused) TextPrimary else AccentCyan,
            modifier = Modifier.size(18.dp)
        )
    }
}

@Composable
private fun TopSearchBarExpanded(
    query: String,
    onQueryChange: (String) -> Unit,
    onClose: () -> Unit,
    focusRequester: FocusRequester,
    modifier: Modifier = Modifier
) {
    var isInputFocused by remember { mutableStateOf(false) }
    var isCloseFocused by remember { mutableStateOf(false) }
    val inputInteraction = remember { MutableInteractionSource() }
    val closeInteraction = remember { MutableInteractionSource() }

    val shape = RoundedCornerShape(22.dp)

    Box(
        modifier = modifier
            .width(300.dp)
            .shadow(
                elevation = 14.dp,
                shape = shape,
                spotColor = AccentCyan
            )
            .clip(shape)
            .background(Color(0x500F172A))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0x55FFFFFF),
                        Color(0x20FFFFFF),
                        Color(0x06000000)
                    )
                )
            )
            .border(
                border = BorderStroke(
                    width = if (isInputFocused) 2.dp else 1.dp,
                    brush = if (isInputFocused) GlassBorderFocusedGradient else GlassBorderGradient
                ),
                shape = shape
            )
            .padding(horizontal = 14.dp, vertical = 8.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = AccentCyan,
                modifier = Modifier.size(17.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

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
                interactionSource = inputInteraction,
                modifier = Modifier
                    .weight(1f)
                    .focusRequester(focusRequester)
                    .onFocusChanged { isInputFocused = it.isFocused },
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

            val hasQuery = query.isNotEmpty()
            Box(
                modifier = Modifier
                    .size(26.dp)
                    .clip(CircleShape)
                    .background(if (isCloseFocused) Color(0x40FFFFFF) else Color.Transparent)
                    .border(
                        border = if (isCloseFocused) BorderStroke(1.dp, Color.White) else BorderStroke(0.dp, Color.Transparent),
                        shape = CircleShape
                    )
                    .onFocusChanged { isCloseFocused = it.isFocused }
                    .focusable(interactionSource = closeInteraction)
                    .clickable(
                        interactionSource = closeInteraction,
                        indication = null,
                        onClick = {
                            if (hasQuery) {
                                onQueryChange("")
                            } else {
                                onClose()
                            }
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (hasQuery) Icons.Default.Clear else Icons.Default.Close,
                    contentDescription = if (hasQuery) "Clear search" else "Close search",
                    tint = if (isCloseFocused) TextPrimary else TextSecondary,
                    modifier = Modifier.size(15.dp)
                )
            }
        }
    }
}

