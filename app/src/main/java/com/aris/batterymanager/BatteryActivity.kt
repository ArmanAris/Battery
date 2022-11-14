package com.aris.batterymanager

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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

        val batteryUsage = BatteryUsage(this)

        val batteryPercentArray: MutableList<BatteryModel> = ArrayList()

        for (item in batteryUsage.getUsageStateList()) {
            if (item.totalTimeInForeground > 0) {
                val bm = BatteryModel()
                val usageTimeApp = item.totalTimeInForeground.toFloat()
                val usageTimeAllApps = batteryUsage.totalTime().toFloat()
                bm.percentUsage = (usageTimeApp / usageTimeAllApps * 100).toInt()
                bm.packageName = item.packageName

                batteryPercentArray.add(bm)
            }
        }

        val sortList = batteryPercentArray.groupBy { it.packageName }
            .mapValues { entry -> entry.value.sumBy { it.percentUsage } } // بعد از تبدیل به هش مپ اولی میشود کلید دومی ولیو
            .toList()     //بعد از تبدیل از هش مپ به لیست، کلید می شود فرست و ولیو می شود سکند
            .sortedWith(compareBy { it.second }).reversed()

        for (item in sortList) {
            // تبدیل درصد استفاده به دقیقه
            val time = item.second.toFloat() / 100 * batteryUsage.totalTime().toFloat() / 1000 / 60
            val hour = time / 60
            val min = time % 60
            Log.e("7171",
                "${item.first} : ${item.second} % And time usage is: ${hour.roundToInt()}:${min.roundToInt()}")
        }

    }

    private var batteryInfo: BroadcastReceiver = object : BroadcastReceiver() {
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

            binding.chargeShow.text = levelBattery.toString() + " %"
            binding.circularProgressBar.setProgressWithAnimation(levelBattery.toFloat())
        }

    }

}