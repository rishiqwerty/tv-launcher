package com.glasstv.launcher.data.model

import android.graphics.drawable.Drawable

data class AppInfo(
    val packageName: String,
    val activityName: String,
    val label: String,
    val banner: Drawable? = null,
    val icon: Drawable? = null,
    val isTvApp: Boolean = true,
    val isFavorite: Boolean = false,
    val installTime: Long = 0L
) {
    val key: String get() = "$packageName/$activityName"
}
