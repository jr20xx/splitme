package io.github.hidroh.splitme

import android.accessibilityservice.AccessibilityServiceInfo
import android.app.admin.DevicePolicyManager
import android.content.Context
import android.os.PowerManager
import android.view.accessibility.AccessibilityManager

object Utils {
    fun isServiceEnabled(context: Context): Boolean {
        val manager = context.getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
        for (it in manager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK)) {
            val serviceInfo = it.resolveInfo.serviceInfo
            if (serviceInfo.packageName == context.packageName && serviceInfo.name == SplitToggleService::class.java.name) return true
        }
        return false
    }

    fun isBatteryOptimizationDisabled(context: Context): Boolean {
        return (context.getSystemService(Context.POWER_SERVICE) as PowerManager).isIgnoringBatteryOptimizations(context.packageName)
    }

    fun isDeviceAdminEnabled(context: Context): Boolean {
        val deviceAdmins = (context.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager).activeAdmins
        if (deviceAdmins != null) for (deviceAdmin in deviceAdmins) if (deviceAdmin.packageName == context.packageName) return true
        return false
    }
}