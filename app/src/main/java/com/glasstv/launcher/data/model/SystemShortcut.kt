package com.glasstv.launcher.data.model

import android.content.Intent
import android.provider.Settings

data class SystemShortcut(
    val id: String,
    val title: String,
    val intentAction: String,
    val category: String? = null
) {
    fun toIntent(): Intent {
        val intent = Intent(intentAction)
        category?.let { intent.addCategory(it) }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        return intent
    }

    companion object {
        val DEFAULT_SHORTCUTS = listOf(
            SystemShortcut(
                id = "settings",
                title = "Settings",
                intentAction = Settings.ACTION_SETTINGS
            ),
            SystemShortcut(
                id = "network",
                title = "Network",
                intentAction = Settings.ACTION_WIRELESS_SETTINGS
            ),
            SystemShortcut(
                id = "sound",
                title = "Audio",
                intentAction = Settings.ACTION_SOUND_SETTINGS
            ),
            SystemShortcut(
                id = "apps",
                title = "Manage Apps",
                intentAction = Settings.ACTION_APPLICATION_SETTINGS
            ),
            SystemShortcut(
                id = "home_settings",
                title = "Default Home",
                intentAction = Settings.ACTION_HOME_SETTINGS
            )
        )
    }
}
