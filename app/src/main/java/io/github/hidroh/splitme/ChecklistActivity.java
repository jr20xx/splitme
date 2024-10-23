package io.github.hidroh.splitme;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import io.github.hidroh.splitme.databinding.ChecklistLayoutBinding;

public class ChecklistActivity extends AppCompatActivity
{
    private ChecklistLayoutBinding binding;

    @SuppressLint("BatteryLife")
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.checklist_layout);
        binding.enableServiceButton.setOnClickListener((v) -> startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY)));
        binding.disableOptimizationsButton.setOnClickListener((v) ->
                startActivity(new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS, Uri.parse("package:" + getPackageName())))
        );
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        binding.enableServiceButton.setEnabled(!Utils.isServiceEnabled(this));
        binding.disableOptimizationsButton.setEnabled(!Utils.isBatteryOptimizationDisabled(this));
    }
}