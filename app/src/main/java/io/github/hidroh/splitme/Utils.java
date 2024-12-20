package io.github.hidroh.splitme;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.os.PowerManager;
import android.view.accessibility.AccessibilityManager;

public class Utils
{
    public static boolean isServiceEnabled(Context context)
    {
        AccessibilityManager manager = (AccessibilityManager) context.getSystemService(Context.ACCESSIBILITY_SERVICE);
        for (AccessibilityServiceInfo it : manager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK))
        {
            var serviceInfo = it.getResolveInfo().serviceInfo;
            if (serviceInfo.packageName.equals(context.getPackageName()) && serviceInfo.name.equals(SplitToggleService.class.getName()))
                return true;
        }
        return false;
    }

    public static boolean isBatteryOptimizationDisabled(Context context)
    {
        return ((PowerManager) context.getSystemService(Context.POWER_SERVICE)).isIgnoringBatteryOptimizations(context.getPackageName());
    }

    public static boolean isDeviceAdminEnabled(Context context)
    {
        var deviceAdmins = ((DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE)).getActiveAdmins();
        if (deviceAdmins != null)
            for (var deviceAdmin : deviceAdmins)
                if (deviceAdmin.getPackageName().equals(context.getPackageName()))
                    return true;
        return false;
    }
}
