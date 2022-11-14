package com.aris.batterymanager

import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.provider.Settings
import java.util.Calendar

class BatteryUsage(private val context: Context) {

    init {
        if (getUsageStateList().isEmpty()) {
            context.startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
        }
    }

    fun getUsageStateList(): List<UsageStats> {
        val usm = context.getSystemService("usagestats") as UsageStatsManager

        val calendar = Calendar.getInstance()
        val endTime = calendar.timeInMillis
        calendar.add(Calendar.YEAR, -1)
        val startTime = calendar.timeInMillis

        return usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, startTime, endTime)
    }

}