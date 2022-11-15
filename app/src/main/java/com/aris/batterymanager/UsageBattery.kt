package com.aris.batterymanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aris.batterymanager.adapter.BatteryUsageAdapter
import com.aris.batterymanager.data.model.BatteryModel
import kotlin.math.roundToInt

class UsageBattery : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_usage_battery)

        val batteryUsage = BatteryAppUsage(this)

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

        val recyclerView = findViewById<RecyclerView>(R.id.recycle1)

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter =
            BatteryUsageAdapter(this, batteryPercentArray, batteryUsage.totalTime())


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
}