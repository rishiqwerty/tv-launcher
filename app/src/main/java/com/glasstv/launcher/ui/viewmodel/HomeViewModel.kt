package com.glasstv.launcher.ui.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.glasstv.launcher.data.model.AppInfo
import com.glasstv.launcher.data.model.LauncherSettings
import com.glasstv.launcher.data.model.SystemShortcut
import com.glasstv.launcher.data.repository.AppRepository
import com.glasstv.launcher.data.repository.SettingsRepository
import com.glasstv.launcher.domain.FavoritesReconciler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class HomeUiState(
    val favoriteApps: List<AppInfo> = emptyList(),
    val allApps: List<AppInfo> = emptyList(),
    val shortcuts: List<SystemShortcut> = SystemShortcut.DEFAULT_SHORTCUTS,
    val settings: LauncherSettings = LauncherSettings(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class HomeViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val appRepository = AppRepository(application)
    private val settingsRepository = SettingsRepository(application)

    private val _errorMessage = MutableStateFlow<String?>(null)

    val uiState: StateFlow<HomeUiState> = combine(
        appRepository.installedApps,
        settingsRepository.favoritePackagesFlow,
        settingsRepository.settingsFlow,
        appRepository.isLoading,
        _errorMessage
    ) { apps, favoritePkgs, settings, loading, error ->
        val favorites = FavoritesReconciler.reconcileFavorites(
            savedFavorites = favoritePkgs,
            installedApps = apps
        )
        val markedApps = FavoritesReconciler.markFavorites(
            favoritePackages = favorites.map { it.packageName }.toSet(),
            installedApps = apps
        )
        android.util.Log.d("GlassLauncher", "HomeViewModel: combine emitted apps=${apps.size}, favorites=${favorites.size}")
        HomeUiState(
            favoriteApps = favorites,
            allApps = markedApps,
            shortcuts = SystemShortcut.DEFAULT_SHORTCUTS,
            settings = settings,
            isLoading = loading,
            errorMessage = error
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeUiState(isLoading = true)
    )

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            appRepository.refreshApps()
        }
    }

    fun launchApp(app: AppInfo): Boolean {
        val success = appRepository.launchApp(app)
        if (!success) {
            _errorMessage.value = "Failed to open ${app.label}"
        }
        return success
    }

    fun launchShortcut(context: Context, shortcut: SystemShortcut): Boolean {
        return try {
            val intent = shortcut.toIntent()
            context.startActivity(intent)
            true
        } catch (e: Exception) {
            _errorMessage.value = "Unable to open ${shortcut.title}"
            false
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}
