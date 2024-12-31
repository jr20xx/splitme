package io.github.hidroh.splitme

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import java.util.Objects

class InvisibleActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        when (Objects.requireNonNull(intent.action)) {
            Constants.ACTION_CHECK_SPLIT_SCREEN -> sendBroadcast(Intent(Constants.ACTION_SPLIT_SCREEN_CHECKED).putExtra(Constants.EXTRA_IS_IN_SPLIT_SCREEN, this.isInMultiWindowMode))
            Constants.ACTION_TOGGLE_SPLIT_SCREEN -> sendBroadcast(Intent(Constants.ACTION_TOGGLE_SPLIT_SCREEN))
        }
        finish()
    }
}