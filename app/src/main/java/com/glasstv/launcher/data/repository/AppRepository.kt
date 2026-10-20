package com.glasstv.launcher.data.repository

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import com.glasstv.launcher.data.model.AppInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext

class AppRepository(
    private val context: Context
) {
    private val packageManager: PackageManager = context.packageManager

    private val _installedApps = MutableStateFlow<List<AppInfo>>(emptyList())
    val installedApps: StateFlow<List<AppInfo>> = _installedApps.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    suspend fun refreshApps(includeMobileApps: Boolean = true): List<AppInfo> = withContext(Dispatchers.IO) {
        _isLoading.value = true
        Log.d("GlassLauncher", "AppRepository: refreshApps started (includeMobileApps=$includeMobileApps)")
        try {
            val selfPackage = context.packageName
            val tvIntent = Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LEANBACK_LAUNCHER)

            val tvResolves = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                packageManager.queryIntentActivities(
                    tvIntent,
                    PackageManager.ResolveInfoFlags.of(PackageManager.MATCH_ALL.toLong())
                ).ifEmpty {
                    packageManager.queryIntentActivities(tvIntent, PackageManager.ResolveInfoFlags.of(0L))
                }
            } else {
                packageManager.queryIntentActivities(tvIntent, PackageManager.MATCH_ALL).ifEmpty {
                    packageManager.queryIntentActivities(tvIntent, 0)
                }
            }

            Log.d("GlassLauncher", "AppRepository: found ${tvResolves.size} leanback activities")

            val tvPackages = mutableSetOf<String>()
            val appList = mutableListOf<AppInfo>()

            for (resolve in tvResolves) {
                val pkgName = resolve.activityInfo.packageName
                if (pkgName == selfPackage) continue
                tvPackages.add(pkgName)

                val label = try {
                    resolve.loadLabel(packageManager).toString()
                } catch (e: Exception) {
                    pkgName
                }

                val banner = try {
                    resolve.activityInfo.loadBanner(packageManager)
                } catch (e: Exception) {
                    null
                }

                val icon = try {
                    resolve.loadIcon(packageManager)
                } catch (e: Exception) {
                    null
                }

                appList.add(
                    AppInfo(
                        packageName = pkgName,
                        activityName = resolve.activityInfo.name,
                        label = label,
                        banner = banner,
                        icon = icon,
                        isTvApp = true
                    )
                )
            }

            if (includeMobileApps) {
                val mobileIntent = Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER)
                val mobileResolves = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    packageManager.queryIntentActivities(
                        mobileIntent,
                        PackageManager.ResolveInfoFlags.of(PackageManager.MATCH_ALL.toLong())
                    ).ifEmpty {
                        packageManager.queryIntentActivities(mobileIntent, PackageManager.ResolveInfoFlags.of(0L))
                    }
                } else {
                    packageManager.queryIntentActivities(mobileIntent, PackageManager.MATCH_ALL).ifEmpty {
                        packageManager.queryIntentActivities(mobileIntent, 0)
                    }
                }

                Log.d("GlassLauncher", "AppRepository: found ${mobileResolves.size} mobile activities")

                for (resolve in mobileResolves) {
                    val pkgName = resolve.activityInfo.packageName
                    if (pkgName == selfPackage || tvPackages.contains(pkgName)) continue

                    val label = try {
                        resolve.loadLabel(packageManager).toString()
                    } catch (e: Exception) {
                        pkgName
                    }

                    val icon = try {
                        resolve.loadIcon(packageManager)
                    } catch (e: Exception) {
                        null
                    }

                    appList.add(
                        AppInfo(
                            packageName = pkgName,
                            activityName = resolve.activityInfo.name,
                            label = label,
                            banner = null,
                            icon = icon,
                            isTvApp = false
                        )
                    )
                }
            }

            appList.sortBy { it.label.lowercase() }
            Log.d("GlassLauncher", "AppRepository: total apps discovered: ${appList.size}")
            _installedApps.value = appList
            appList
        } catch (e: Exception) {
            Log.e("GlassLauncher", "AppRepository: Error refreshing apps", e)
            emptyList()
        } finally {
            _isLoading.value = false
        }
    }

    fun launchApp(app: AppInfo): Boolean {
        return try {
            val launchIntent = packageManager.getLeanbackLaunchIntentForPackage(app.packageName)
                ?: packageManager.getLaunchIntentForPackage(app.packageName)
                ?: Intent().setComponent(ComponentName(app.packageName, app.activityName))

            launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(launchIntent)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
