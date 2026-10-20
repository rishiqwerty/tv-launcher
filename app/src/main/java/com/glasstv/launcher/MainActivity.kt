package com.glasstv.launcher

import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.addCallback
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import com.glasstv.launcher.data.receiver.PackageChangeReceiver
import com.glasstv.launcher.ui.components.AppActionDialog
import com.glasstv.launcher.ui.components.LauncherScreen
import com.glasstv.launcher.ui.screens.AllAppsScreen
import com.glasstv.launcher.ui.screens.HomeScreen
import com.glasstv.launcher.ui.screens.SettingsScreen
import com.glasstv.launcher.ui.theme.TVLauncherTheme
import com.glasstv.launcher.ui.viewmodel.HomeViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: HomeViewModel by viewModels()

    private val packageReceiver = PackageChangeReceiver {
        viewModel.refresh()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Register package change receiver for live app addition/removal
        val filter = IntentFilter().apply {
            addAction(Intent.ACTION_PACKAGE_ADDED)
            addAction(Intent.ACTION_PACKAGE_REMOVED)
            addAction(Intent.ACTION_PACKAGE_REPLACED)
            addDataScheme("package")
        }
        registerReceiver(packageReceiver, filter)

        // Remote Back button navigation
        onBackPressedDispatcher.addCallback(this) {
            val handled = viewModel.handleBack()
            if (!handled) {
                // Already on Home screen; keep launcher active without exiting
            }
        }

        setContent {
            val uiState by viewModel.uiState.collectAsState()
            val currentScreen by viewModel.currentScreen.collectAsState()
            val selectedAppForAction by viewModel.selectedAppForAction.collectAsState()
            val context = LocalContext.current

            TVLauncherTheme(settings = uiState.settings) {
                when (currentScreen) {
                    LauncherScreen.HOME -> {
                        HomeScreen(
                            uiState = uiState,
                            onAppClick = { app ->
                                val success = viewModel.launchApp(app)
                                if (!success) {
                                    Toast.makeText(context, "Unable to launch ${app.label}", Toast.LENGTH_SHORT).show()
                                }
                            },
                            onAppLongClick = { app ->
                                viewModel.selectAppForAction(app)
                            },
                            onShortcutClick = { shortcut ->
                                val success = viewModel.launchShortcut(context, shortcut)
                                if (!success) {
                                    Toast.makeText(context, "Shortcut ${shortcut.title} not available", Toast.LENGTH_SHORT).show()
                                }
                            },
                            onScreenSelected = { screen ->
                                viewModel.setScreen(screen)
                            }
                        )
                    }

                    LauncherScreen.ALL_APPS -> {
                        AllAppsScreen(
                            uiState = uiState,
                            onAppClick = { app ->
                                val success = viewModel.launchApp(app)
                                if (!success) {
                                    Toast.makeText(context, "Unable to launch ${app.label}", Toast.LENGTH_SHORT).show()
                                }
                            },
                            onAppLongClick = { app ->
                                viewModel.selectAppForAction(app)
                            },
                            onScreenSelected = { screen ->
                                viewModel.setScreen(screen)
                            }
                        )
                    }

                    LauncherScreen.SETTINGS -> {
                        SettingsScreen(
                            uiState = uiState,
                            onUpdateSettings = { transform ->
                                viewModel.updateSettings(transform)
                            },
                            onScreenSelected = { screen ->
                                viewModel.setScreen(screen)
                            }
                        )
                    }
                }

                // Modal Action Dialog for remote-friendly Favorites management
                selectedAppForAction?.let { app ->
                    AppActionDialog(
                        app = app,
                        onDismiss = { viewModel.selectAppForAction(null) },
                        onOpen = { viewModel.launchApp(it) },
                        onToggleFavorite = { viewModel.toggleFavorite(it) },
                        onMoveLeft = { viewModel.moveFavoriteLeft(it) },
                        onMoveRight = { viewModel.moveFavoriteRight(it) },
                        onAppInfo = {
                            try {
                                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                    data = Uri.fromParts("package", it.packageName, null)
                                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                }
                                context.startActivity(intent)
                            } catch (e: Exception) {
                                Toast.makeText(context, "App details unavailable", Toast.LENGTH_SHORT).show()
                            }
                        }
                    )
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.refresh()
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            unregisterReceiver(packageReceiver)
        } catch (e: Exception) {
            // Ignored
        }
    }
}
