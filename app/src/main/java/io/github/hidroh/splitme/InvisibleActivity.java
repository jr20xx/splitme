package io.github.hidroh.splitme;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import java.util.Objects;

public class InvisibleActivity extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);
        switch (Objects.requireNonNull(getIntent().getAction()))
        {
            case Constants.ACTION_CHECK_SPLIT_SCREEN:
                broadcastManager.sendBroadcast(new Intent(Constants.ACTION_SPLIT_SCREEN_CHECKED).putExtra(Constants.EXTRA_IS_IN_SPLIT_SCREEN, this.isInMultiWindowMode()));
                break;
            case Constants.ACTION_TOGGLE_SPLIT_SCREEN:
                broadcastManager.sendBroadcast(new Intent(Constants.ACTION_TOGGLE_SPLIT_SCREEN));
                break;
        }
        finish();
    }
}
