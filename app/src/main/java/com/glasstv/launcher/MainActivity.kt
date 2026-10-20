package com.glasstv.launcher

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import com.glasstv.launcher.ui.screens.HomeScreen
import com.glasstv.launcher.ui.theme.GlassTVTheme
import com.glasstv.launcher.ui.viewmodel.HomeViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val uiState by viewModel.uiState.collectAsState()
            val context = LocalContext.current

            GlassTVTheme(settings = uiState.settings) {
                HomeScreen(
                    uiState = uiState,
                    onAppClick = { app ->
                        val success = viewModel.launchApp(app)
                        if (!success) {
                            Toast.makeText(context, "Unable to launch ${app.label}", Toast.LENGTH_SHORT).show()
                        }
                    },
                    onShortcutClick = { shortcut ->
                        val success = viewModel.launchShortcut(context, shortcut)
                        if (!success) {
                            Toast.makeText(context, "Shortcut ${shortcut.title} not available", Toast.LENGTH_SHORT).show()
                        }
                    }
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.refresh()
    }
}
