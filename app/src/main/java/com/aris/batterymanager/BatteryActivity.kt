package com.aris.batterymanager

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class BatteryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_battery)

        registerReceiver(batteryInfo, IntentFilter(Intent.ACTION_BATTERY_CHANGED))

    }

    private var batteryInfo: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0)
            intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0)
            intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0) / 10
            intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0) / 1000
            intent.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY)
        }

    }

}