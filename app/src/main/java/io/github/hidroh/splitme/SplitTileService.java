package io.github.hidroh.splitme;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.drawable.Icon;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class SplitTileService extends TileService
{
    private LocalBroadcastManager localBroadcastManager;
    private boolean isActive = false;
    private int iconResId = R.drawable.ic_split_black_24dp;
    private final BroadcastReceiver receiver = new BroadcastReceiver()
    {
        public void onReceive(Context context, Intent intent)
        {
            updateTileState(intent.getBooleanExtra(Constants.EXTRA_IS_IN_SPLIT_SCREEN, false));
        }
    };

    public void onStartListening()
    {
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.registerReceiver(receiver, new IntentFilter(Constants.ACTION_SPLIT_SCREEN_CHECKED));
        updateIcon(this.getResources().getConfiguration());
        checkSplitScreen();
    }

    public void onStopListening()
    {
        localBroadcastManager.unregisterReceiver(receiver);
    }

    public void onClick()
    {
        if (Utils.isServiceEnabled(getApplicationContext()) && Utils.isBatteryOptimizationDisabled(getApplicationContext()))
        {
            isActive = !isActive;
            updateTileState(isActive);
            toggleAndCollapse();
        }
        else
            startActivity(new Intent(getApplicationContext(), ChecklistActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    public void onConfigurationChanged(Configuration newConfig)
    {
        updateIcon(newConfig);
        updateTileState(isActive);
    }

    private void toggleAndCollapse()
    {
        startActivityAndCollapse(new Intent(this, InvisibleActivity.class)
                .setAction(Constants.ACTION_TOGGLE_SPLIT_SCREEN)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    private void updateTileState(boolean active)
    {
        Tile tile = getQsTile();
        if (tile != null)
        {
            tile.setState(active ? Tile.STATE_ACTIVE : Tile.STATE_INACTIVE);
            tile.setLabel(getString(active ? R.string.label_on : R.string.label_off));
            tile.setIcon(Icon.createWithResource(this, iconResId));
            tile.updateTile();
        }
    }

    private void checkSplitScreen()
    {
        startActivity(new Intent(this, InvisibleActivity.class)
                .setAction(Constants.ACTION_CHECK_SPLIT_SCREEN)
                .setFlags(Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT | Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    private void updateIcon(Configuration newConfig)
    {
        iconResId = (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) ?
                R.drawable.ic_split_land_black_24dp : R.drawable.ic_split_black_24dp;
    }
}