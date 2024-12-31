package io.github.hidroh.splitme

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Configuration
import android.graphics.drawable.Icon
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import androidx.core.content.ContextCompat
import io.github.hidroh.splitme.Utils.isBatteryOptimizationDisabled
import io.github.hidroh.splitme.Utils.isServiceEnabled

class SplitTileService : TileService() {
    private var isActive = false
    private var iconResId = R.drawable.ic_split_black_24dp
    private val receiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            updateTileState(intent.getBooleanExtra(Constants.EXTRA_IS_IN_SPLIT_SCREEN, false))
        }
    }

    override fun onStartListening() {
        ContextCompat.registerReceiver(this, receiver, IntentFilter(Constants.ACTION_SPLIT_SCREEN_CHECKED), ContextCompat.RECEIVER_NOT_EXPORTED)
        updateIcon(resources.configuration)
        startActivity(Intent(this, InvisibleActivity::class.java)
                .setAction(Constants.ACTION_CHECK_SPLIT_SCREEN)
                .setFlags(Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT or Intent.FLAG_ACTIVITY_NEW_TASK))
    }

    override fun onStopListening() {
        unregisterReceiver(receiver)
    }

    override fun onClick() {
        if (isServiceEnabled(this) && isBatteryOptimizationDisabled(this)) {
            isActive = !isActive
            updateTileState(isActive)
            startActivityAndCollapse(Intent(this, InvisibleActivity::class.java)
                    .setAction(Constants.ACTION_TOGGLE_SPLIT_SCREEN).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        } else startActivityAndCollapse(Intent(this, ChecklistActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_NO_USER_ACTION))
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        updateIcon(newConfig)
        updateTileState(isActive)
    }

    private fun updateTileState(active: Boolean) {
        val tile = qsTile
        if (tile != null) {
            tile.state = if (active) Tile.STATE_ACTIVE else Tile.STATE_INACTIVE
            tile.label = getString(if (active) R.string.label_on else R.string.label_off)
            tile.icon = Icon.createWithResource(this, iconResId)
            tile.updateTile()
        }
    }

    private fun updateIcon(newConfig: Configuration) {
        iconResId = if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) R.drawable.ic_split_land_black_24dp else R.drawable.ic_split_black_24dp
    }
}