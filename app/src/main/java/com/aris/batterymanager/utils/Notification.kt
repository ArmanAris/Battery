package com.aris.batterymanager.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.aris.batterymanager.R


class Notification : Service() {  // android.app.Service

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createNotification()
        startNotification()

        return START_STICKY   // در هر حالت سرویس در حال اجرا باشد با بسته شدن نرم افزار قطع نشود
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }


    // Create And Start Notification
    private fun createNotification() {
        val serviceChannel =
            NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_MIN)
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(serviceChannel)
    }

    private fun startNotification() {
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("")
            .setContentText("")
            .setSmallIcon(R.drawable.battery)
            .build()
//اگر چند نوتیفیکیشن داشته باشیم و از یک آیدی استفاده کنیم،نوتیفیکیشن جدید روی قیلی نوشته می شود
        startForeground(NOTIFICATION_ID, notification)
    }


    companion object {
        const val CHANNEL_ID = "BatteryManagerChannel"
        const val CHANNEL_NAME = "BatteryManagerService"
        const val NOTIFICATION_ID = 1
    }

}