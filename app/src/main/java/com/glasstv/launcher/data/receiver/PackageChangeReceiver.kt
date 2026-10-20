package com.glasstv.launcher.data.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class PackageChangeReceiver(
    private val onPackageChanged: ((packageName: String) -> Unit)? = null
) : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val packageName = intent?.data?.schemeSpecificPart ?: return
        when (intent.action) {
            Intent.ACTION_PACKAGE_ADDED,
            Intent.ACTION_PACKAGE_REMOVED,
            Intent.ACTION_PACKAGE_REPLACED -> {
                onPackageChanged?.invoke(packageName)
            }
        }
    }
}
