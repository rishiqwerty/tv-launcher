package com.glasstv.launcher.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.tv.material3.Text
import com.glasstv.launcher.ui.theme.TextMuted
import com.glasstv.launcher.ui.theme.TextPrimary
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ClockWidget(
    modifier: Modifier = Modifier,
    is24Hour: Boolean = false
) {
    var currentTime by remember { mutableStateOf(Date()) }

    LaunchedEffect(Unit) {
        while (true) {
            currentTime = Date()
            delay(1000L * 10) // Lifecycle-friendly 10-second tick
        }
    }

    val timeFormat = remember(is24Hour) {
        if (is24Hour) SimpleDateFormat("HH:mm", Locale.getDefault())
        else SimpleDateFormat("h:mm a", Locale.getDefault())
    }

    val dateFormat = remember {
        SimpleDateFormat("EEEE, MMMM d", Locale.getDefault())
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.End
    ) {
        Text(
            text = timeFormat.format(currentTime),
            color = TextPrimary,
            fontSize = 24.sp,
            fontWeight = FontWeight.Light,
            letterSpacing = 0.5.sp
        )
        Text(
            text = dateFormat.format(currentTime),
            color = TextMuted,
            fontSize = 12.sp,
            fontWeight = FontWeight.Normal
        )
    }
}
