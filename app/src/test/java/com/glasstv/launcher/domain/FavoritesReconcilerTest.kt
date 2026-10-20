package com.glasstv.launcher.domain

import com.glasstv.launcher.data.model.AppInfo
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class FavoritesReconcilerTest {

    private fun mockApp(pkg: String, isTv: Boolean = true) = AppInfo(
        packageName = pkg,
        activityName = "$pkg.MainActivity",
        label = pkg.substringAfterLast("."),
        isTvApp = isTv
    )

    @Test
    fun `reconcileFavorites with empty saved list selects first TV apps`() {
        val apps = listOf(
            mockApp("com.netflix.ninja", isTv = true),
            mockApp("com.youtube", isTv = true),
            mockApp("com.mobile.app", isTv = false)
        )

        val favorites = FavoritesReconciler.reconcileFavorites(
            savedFavorites = emptyList(),
            installedApps = apps,
            defaultMaxCount = 2
        )

        assertEquals(2, favorites.size)
        assertEquals("com.netflix.ninja", favorites[0].packageName)
        assertEquals("com.youtube", favorites[1].packageName)
        assertTrue(favorites[0].isFavorite)
    }

    @Test
    fun `reconcileFavorites preserves saved order`() {
        val apps = listOf(
            mockApp("com.netflix.ninja"),
            mockApp("com.youtube"),
            mockApp("com.amazon.prime")
        )

        val savedOrder = listOf("com.amazon.prime", "com.netflix.ninja")

        val favorites = FavoritesReconciler.reconcileFavorites(
            savedFavorites = savedOrder,
            installedApps = apps
        )

        assertEquals(2, favorites.size)
        assertEquals("com.amazon.prime", favorites[0].packageName)
        assertEquals("com.netflix.ninja", favorites[1].packageName)
    }

    @Test
    fun `reconcileFavorites handles uninstalled app safely`() {
        val apps = listOf(
            mockApp("com.youtube")
        )

        val savedOrder = listOf("com.uninstalled.app", "com.youtube")

        val favorites = FavoritesReconciler.reconcileFavorites(
            savedFavorites = savedOrder,
            installedApps = apps
        )

        assertEquals(1, favorites.size)
        assertEquals("com.youtube", favorites[0].packageName)
    }

    @Test
    fun `markFavorites marks apps correctly`() {
        val apps = listOf(
            mockApp("com.youtube"),
            mockApp("com.netflix")
        )

        val marked = FavoritesReconciler.markFavorites(
            favoritePackages = setOf("com.youtube"),
            installedApps = apps
        )

        assertTrue(marked.first { it.packageName == "com.youtube" }.isFavorite)
        assertFalse(marked.first { it.packageName == "com.netflix" }.isFavorite)
    }
}
