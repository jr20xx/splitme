package io.github.hidroh.splitme;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.ComponentName;
import android.content.Context;
import android.os.PowerManager;
import android.view.accessibility.AccessibilityManager;

public class Utils
{
    public static boolean isServiceEnabled(Context context)
    {
        AccessibilityManager manager = (AccessibilityManager) context.getSystemService(Context.ACCESSIBILITY_SERVICE);
        if (!manager.isEnabled()) return false;
        String serviceId = new ComponentName(context, SplitToggleService.class).flattenToShortString();
        for (AccessibilityServiceInfo it : manager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_GENERIC))
            if (it.getId().equals(serviceId)) return true;
        return false;
    }

    public static boolean isBatteryOptimizationDisabled(Context context)
    {
        return ((PowerManager) context.getSystemService(Context.POWER_SERVICE)).isIgnoringBatteryOptimizations(context.getPackageName());
    }
}
