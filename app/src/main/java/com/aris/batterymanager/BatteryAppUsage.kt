package com.aris.batterymanager

import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.provider.Settings

class BatteryAppUsage(private val context: Context) {

    init { // اول اجرا می شود
        if (getUsageStateList().isEmpty()) {  // اگر لیست خالی بود دسترسی بده
            context.startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
        }
    }

    //  گرفتن اطلاعات مصرفی یک روز برنامه ها به صورت لیست
    fun getUsageStateList(): List<UsageStats> {
        val usm = context.getSystemService("usagestats") as UsageStatsManager

        val endTime = System.currentTimeMillis()
        val startTime = System.currentTimeMillis() - 24 * 3600 * 1000  // یک روز کم

        return usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, startTime, endTime)
    }

    // مجموع مدت زمان برنامه ها
    fun totalTime(): Long {
        var totalTime: Long = 0
        for (item in getUsageStateList()) {
            totalTime += item.totalTimeInForeground
        }
        return totalTime
    }

}