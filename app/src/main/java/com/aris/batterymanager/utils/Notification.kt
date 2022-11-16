package com.aris.batterymanager.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder


class Notification : Service() {  // android.app.Service

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {


        return START_STICKY   // در هر حالت سرویس در حال اجرا باشد با بسته شدن نرم افزار قطع نشود
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun createNotification() {
        val serviceChannel =
            NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_MIN)
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(serviceChannel)
    }

    companion object {
        const val CHANNEL_ID = "BatteryManagerChannel"
        const val CHANNEL_NAME = "BatteryManagerService"
    }

}