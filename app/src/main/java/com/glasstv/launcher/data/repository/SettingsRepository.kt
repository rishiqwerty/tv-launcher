package com.glasstv.launcher.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.glasstv.launcher.data.model.BlurIntensity
import com.glasstv.launcher.data.model.LauncherSettings
import com.glasstv.launcher.data.model.ThemeMode
import com.glasstv.launcher.data.model.UiDensity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "launcher_settings")

class SettingsRepository(
    private val context: Context
) {
    private object Keys {
        val THEME_MODE = stringPreferencesKey("theme_mode")
        val BLUR_INTENSITY = stringPreferencesKey("blur_intensity")
        val UI_DENSITY = stringPreferencesKey("ui_density")
        val REDUCED_MOTION = booleanPreferencesKey("reduced_motion")
        val SHOW_CLOCK = booleanPreferencesKey("show_clock")
        val CLOCK_FORMAT_24H = booleanPreferencesKey("clock_format_24h")
        val INCLUDE_MOBILE_APPS = booleanPreferencesKey("include_mobile_apps")
        val FAVORITE_PACKAGES_ORDER = stringPreferencesKey("favorite_packages_order")
    }

    val settingsFlow: Flow<LauncherSettings> = context.dataStore.data.map { prefs ->
        LauncherSettings(
            themeMode = ThemeMode.valueOf(prefs[Keys.THEME_MODE] ?: ThemeMode.DARK.name),
            blurIntensity = BlurIntensity.valueOf(prefs[Keys.BLUR_INTENSITY] ?: BlurIntensity.MEDIUM.name),
            uiDensity = UiDensity.valueOf(prefs[Keys.UI_DENSITY] ?: UiDensity.COMFORTABLE.name),
            reducedMotion = prefs[Keys.REDUCED_MOTION] ?: false,
            showClock = prefs[Keys.SHOW_CLOCK] ?: true,
            clockFormat24Hour = prefs[Keys.CLOCK_FORMAT_24H] ?: false,
            includeMobileApps = prefs[Keys.INCLUDE_MOBILE_APPS] ?: true
        )
    }

    val favoritePackagesFlow: Flow<List<String>> = context.dataStore.data.map { prefs ->
        val raw = prefs[Keys.FAVORITE_PACKAGES_ORDER] ?: ""
        if (raw.isBlank()) emptyList() else raw.split(",")
    }

    suspend fun updateSettings(transform: (LauncherSettings) -> LauncherSettings) {
        context.dataStore.edit { prefs ->
            val current = LauncherSettings(
                themeMode = ThemeMode.valueOf(prefs[Keys.THEME_MODE] ?: ThemeMode.DARK.name),
                blurIntensity = BlurIntensity.valueOf(prefs[Keys.BLUR_INTENSITY] ?: BlurIntensity.MEDIUM.name),
                uiDensity = UiDensity.valueOf(prefs[Keys.UI_DENSITY] ?: UiDensity.COMFORTABLE.name),
                reducedMotion = prefs[Keys.REDUCED_MOTION] ?: false,
                showClock = prefs[Keys.SHOW_CLOCK] ?: true,
                clockFormat24Hour = prefs[Keys.CLOCK_FORMAT_24H] ?: false,
                includeMobileApps = prefs[Keys.INCLUDE_MOBILE_APPS] ?: true
            )
            val updated = transform(current)
            prefs[Keys.THEME_MODE] = updated.themeMode.name
            prefs[Keys.BLUR_INTENSITY] = updated.blurIntensity.name
            prefs[Keys.UI_DENSITY] = updated.uiDensity.name
            prefs[Keys.REDUCED_MOTION] = updated.reducedMotion
            prefs[Keys.SHOW_CLOCK] = updated.showClock
            prefs[Keys.CLOCK_FORMAT_24H] = updated.clockFormat24Hour
            prefs[Keys.INCLUDE_MOBILE_APPS] = updated.includeMobileApps
        }
    }

    suspend fun saveFavorites(packages: List<String>) {
        context.dataStore.edit { prefs ->
            prefs[Keys.FAVORITE_PACKAGES_ORDER] = packages.joinToString(",")
        }
    }
}
