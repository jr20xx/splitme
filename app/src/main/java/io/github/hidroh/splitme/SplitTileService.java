package io.github.hidroh.splitme;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.drawable.Icon;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

public class SplitTileService extends TileService
{
    private boolean isActive = false;
    private int iconResId = R.drawable.ic_split_black_24dp;
    private final BroadcastReceiver receiver = new BroadcastReceiver()
    {
        public void onReceive(Context context, @NonNull Intent intent)
        {
            updateTileState(intent.getBooleanExtra(Constants.EXTRA_IS_IN_SPLIT_SCREEN, false));
        }
    };

    @Override
    public void onStartListening()
    {
        ContextCompat.registerReceiver(this, receiver, new IntentFilter(Constants.ACTION_SPLIT_SCREEN_CHECKED), ContextCompat.RECEIVER_NOT_EXPORTED);
        updateIcon(getResources().getConfiguration());
        startActivity(new Intent(this, InvisibleActivity.class)
                .setAction(Constants.ACTION_CHECK_SPLIT_SCREEN)
                .setFlags(Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT | Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    public void onStopListening()
    {
        unregisterReceiver(receiver);
    }

    @Override
    public void onClick()
    {
        if (Utils.isServiceEnabled(this) && Utils.isBatteryOptimizationDisabled(this))
        {
            isActive = !isActive;
            updateTileState(isActive);
            startActivityAndCollapse(new Intent(this, InvisibleActivity.class)
                    .setAction(Constants.ACTION_TOGGLE_SPLIT_SCREEN).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
        else
            startActivityAndCollapse(new Intent(this, ChecklistActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NO_USER_ACTION));
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        updateIcon(newConfig);
        updateTileState(isActive);
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

    private void updateIcon(@NonNull Configuration newConfig)
    {
        iconResId = (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) ?
                R.drawable.ic_split_land_black_24dp : R.drawable.ic_split_black_24dp;
    }
}