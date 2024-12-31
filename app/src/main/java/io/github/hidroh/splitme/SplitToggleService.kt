package io.github.hidroh.splitme

import android.accessibilityservice.AccessibilityService
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.view.accessibility.AccessibilityEvent
import androidx.core.content.ContextCompat

class SplitToggleService : AccessibilityService() {
    private val receiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            performGlobalAction(GLOBAL_ACTION_TOGGLE_SPLIT_SCREEN)
        }
    }

    override fun onCreate() {
        ContextCompat.registerReceiver(this, receiver, IntentFilter(ACTION_TOGGLE_SPLIT_SCREEN), ContextCompat.RECEIVER_NOT_EXPORTED)
    }

    override fun onInterrupt() {}
    override fun onAccessibilityEvent(event: AccessibilityEvent) {}
    override fun onDestroy() {
        unregisterReceiver(receiver)
    }
}