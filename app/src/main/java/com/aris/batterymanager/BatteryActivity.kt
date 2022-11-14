package com.aris.batterymanager

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.aris.batterymanager.databinding.ActivityBatteryBinding
import com.aris.batterymanager.databinding.ActivityMainBinding

class BatteryActivity : AppCompatActivity() {

    lateinit var binding: ActivityBatteryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBatteryBinding.inflate(layoutInflater)
        setContentView(binding.root)



        registerReceiver(batteryInfo, IntentFilter(Intent.ACTION_BATTERY_CHANGED))

    }

    private var batteryInfo: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0)

            if (intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0) == 0){
                binding.plugNum.text ="متصل نیست"
            }else{
                binding.plugNum.text ="متصل است"
            }
            binding.tempNum.text =
                (intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0) / 10).toString() + " C°"
            binding.voltageNum.text =
                (intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0) / 1000).toString() + " Volt"
            binding.tecNum.text = intent.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY)
        }

    }

}