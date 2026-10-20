package com.glasstv.launcher.domain

import com.glasstv.launcher.data.model.AppInfo

object FavoritesReconciler {

    /**
     * Reconciles persisted favorite package names against currently installed apps.
     * Preserves user-defined order.
     * If savedFavorites is empty (e.g., initial run), defaults to first TV apps.
     */
    fun reconcileFavorites(
        savedFavorites: List<String>,
        installedApps: List<AppInfo>,
        defaultMaxCount: Int = 8
    ): List<AppInfo> {
        if (installedApps.isEmpty()) return emptyList()

        val appMap = installedApps.associateBy { it.packageName }

        if (savedFavorites.isEmpty()) {
            // Default to first TV apps
            val defaults = installedApps
                .filter { it.isTvApp }
                .take(defaultMaxCount)
                .ifEmpty { installedApps.take(defaultMaxCount) }

            return defaults.map { it.copy(isFavorite = true) }
        }

        val result = mutableListOf<AppInfo>()
        for (pkg in savedFavorites) {
            val app = appMap[pkg]
            if (app != null) {
                result.add(app.copy(isFavorite = true))
            }
        }
        return result
    }

    /**
     * Tags all installed apps with isFavorite based on whether their package is favorited.
     */
    fun markFavorites(
        favoritePackages: Set<String>,
        installedApps: List<AppInfo>
    ): List<AppInfo> {
        return installedApps.map { app ->
            app.copy(isFavorite = favoritePackages.contains(app.packageName))
        }
    }
}
