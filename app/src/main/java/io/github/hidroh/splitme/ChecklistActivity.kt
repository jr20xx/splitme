package io.github.hidroh.splitme

import android.annotation.SuppressLint
import android.app.admin.DeviceAdminReceiver
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import io.github.hidroh.splitme.Utils.isBatteryOptimizationDisabled
import io.github.hidroh.splitme.Utils.isDeviceAdminEnabled
import io.github.hidroh.splitme.Utils.isServiceEnabled
import io.github.hidroh.splitme.databinding.ChecklistLayoutBinding

class ChecklistActivity : AppCompatActivity() {
    private var binding: ChecklistLayoutBinding? = null
    @SuppressLint("BatteryLife")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.checklist_layout)
        binding.enableServiceButton.setOnClickListener { v: View? -> startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)) }
        binding.disableOptimizationsButton.setOnClickListener { v: View? -> startActivity(Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS, Uri.parse("package:$packageName"))) }
        binding.enableDeviceAdminButton.setOnClickListener { v: View ->
            val adminComponent = ComponentName(this, DeviceAdminReceiver::class.java)
            if (isDeviceAdminEnabled(this)) {
                (getSystemService(DEVICE_POLICY_SERVICE) as DevicePolicyManager).removeActiveAdmin(adminComponent)
                (v as Button).setText(R.string.enable)
            } else startActivity(Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN).putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, adminComponent))
        }
    }

    override fun onResume() {
        super.onResume()
        binding!!.enableServiceButton.isEnabled = !isServiceEnabled(this)
        binding!!.disableOptimizationsButton.isEnabled = !isBatteryOptimizationDisabled(this)
        binding!!.enableDeviceAdminButton.setText(if (isDeviceAdminEnabled(this)) R.string.disable else R.string.enable)
    }
}