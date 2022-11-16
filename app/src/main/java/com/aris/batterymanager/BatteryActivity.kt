package com.aris.batterymanager

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.BatteryManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import com.aris.batterymanager.data.model.BatteryModel
import com.aris.batterymanager.databinding.ActivityBatteryBinding
import kotlin.math.roundToInt

class BatteryActivity : AppCompatActivity() {

    lateinit var binding: ActivityBatteryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBatteryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        registerReceiver(batteryInfo, IntentFilter(Intent.ACTION_BATTERY_CHANGED))

        binding.menu.setOnClickListener {
            binding.drawer.openDrawer(Gravity.RIGHT)
        }

        binding.incLayout.appUsage.setOnClickListener {
            startActivity(Intent(this, UsageBattery::class.java))
            binding.drawer.closeDrawer(Gravity.RIGHT)
        }

    }

    private var batteryInfo: BroadcastReceiver = object : BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        override fun onReceive(context: Context, intent: Intent) {
            val levelBattery = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0)

            if (intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0) == 0) {
                binding.plugNum.text = "متصل نیست"
            } else {
                binding.plugNum.text = "متصل است"
            }
            binding.tempNum.text =
                (intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0) / 10).toString() + " C°"
            binding.voltageNum.text =
                (intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0) / 1000).toString() + " Volt"
            binding.tecNum.text = intent.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY)

            binding.chargeShow.text = "$levelBattery %"
            binding.circularProgressBar.setProgressWithAnimation(levelBattery.toFloat())

            val health = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, 0)

            when (health) {
                BatteryManager.BATTERY_HEALTH_DEAD -> {
                    binding.txtHealth.text = "باتری شما از کار افتاده است"
                    binding.txtHealth.setTextColor(Color.parseColor("#000000"))
                    binding.imageHealth.setImageResource(R.drawable.skullh)
                }
                BatteryManager.BATTERY_HEALTH_GOOD -> {
                    binding.txtHealth.text = "باتری سالم است"
                    binding.txtHealth.setTextColor(Color.GREEN)
                    binding.imageHealth.setImageResource(R.drawable.goodh)
                }
                BatteryManager.BATTERY_HEALTH_COLD -> {
                    binding.txtHealth.text = " باتری خنک و سالم است"
                    binding.txtHealth.setTextColor(Color.BLUE)
                    binding.imageHealth.setImageResource(R.drawable.coldh)
                }
                BatteryManager.BATTERY_HEALTH_OVERHEAT -> {
                    binding.txtHealth.text = "دمای باتری بالا است"
                    binding.txtHealth.setTextColor(Color.RED)
                    binding.imageHealth.setImageResource(R.drawable.thermometerh)
                }
                BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE -> {
                    binding.txtHealth.text = "ولتاژ باتری زیاد است!!!"
                    binding.txtHealth.setTextColor(Color.YELLOW)
                    binding.imageHealth.setImageResource(R.drawable.voltageh)
                }
            }
        }

    }

}