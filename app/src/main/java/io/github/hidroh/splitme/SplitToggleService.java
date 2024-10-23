package io.github.hidroh.splitme;

import android.accessibilityservice.AccessibilityService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.accessibility.AccessibilityEvent;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class SplitToggleService extends AccessibilityService
{
    private LocalBroadcastManager localBroadcastManager;
    private BroadcastReceiver receiver = new BroadcastReceiver()
    {
        public void onReceive(Context context, Intent intent)
        {
            performGlobalAction(GLOBAL_ACTION_TOGGLE_SPLIT_SCREEN);
        }
    };

    public void onCreate()
    {
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.registerReceiver(receiver, new IntentFilter(Constants.ACTION_TOGGLE_SPLIT_SCREEN));
    }

    public void onInterrupt()
    {
    }

    public void onAccessibilityEvent(AccessibilityEvent event)
    {
    }

    public void onDestroy()
    {
        localBroadcastManager.unregisterReceiver(receiver);
    }
}
