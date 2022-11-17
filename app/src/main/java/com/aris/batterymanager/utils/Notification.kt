package com.aris.batterymanager.utils

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.RingtoneManager
import android.net.Uri
import android.os.*
import androidx.core.app.NotificationCompat
import com.aris.batterymanager.R


class Notification : Service() {  // android.app.Service

    private var manager: NotificationManager? = null    //use

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createNotification()
        startNotification()

        // صدا زدن BroadcastReceiver
        registerReceiver(batteryInfo,
            IntentFilter(Intent.ACTION_BATTERY_CHANGED)) //BroadcastReceiver

        return START_STICKY   // در هر حالت سرویس در حال اجرا باشد با بسته شدن نرم افزار قطع نشود
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }


    // به یک سری مسائل گوش می دهد BroadcastReceiver
    // داده ها هر لحطه که تغییر کند عوض می شود
    private var batteryInfo: BroadcastReceiver =
        object : BroadcastReceiver() {    //BroadcastReceiver
            override fun onReceive(context: Context, intent: Intent) {

                val levelBattery = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0)

                val plug = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0)
                var plugState = ""

                if (plug == 0) {
                    plugState = "شارژر متصل نیست"
                } else {
                    plugState = "شارژر متصل است"
                }

                if (levelBattery > 95 && plug == 1) {
                    startAlarm()
                    plugState = "برای سلامتی باتری بهتر است از شارژ خارج شود"
                }

                updateNotification(levelBattery, plugState)
            }
        }


    // Create , Start Notification And Update
    private fun createNotification() {
        val serviceChannel =
            NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_MIN)
        manager = getSystemService(NotificationManager::class.java)    //use
        manager!!.createNotificationChannel(serviceChannel)
    }

    private fun startNotification() {
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("در حال بارگذاری...")
            .setContentText("در حال دریافت اطلاعات باتری")
            .setSmallIcon(R.drawable.battery)
            .build()
//اگر چند نوتیفیکیشن داشته باشیم و از یک آیدی استفاده کنیم،نوتیفیکیشن جدید روی قیلی نوشته می شود
        startForeground(NOTIFICATION_ID, notification)
    }

    private fun updateNotification(battery: Int, plugState: String) {
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(plugState)
            .setContentText("$battery %")
            .setSmallIcon(R.drawable.battery)
            .build()

        manager!!.notify(NOTIFICATION_ID, notification)
    }

    companion object {
        const val CHANNEL_ID = "BatteryManagerChannel"
        const val CHANNEL_NAME = "BatteryManagerService"
        const val NOTIFICATION_ID = 1
    }

    // Add RINGTONE and Vibration
    private fun startAlarm() {
        val alarm: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        // TYPE_RINGTONE , TYPE_NOTIFICATION , TYPE_ALARM And ...
        val ring = RingtoneManager.getRingtone(applicationContext, alarm)
        ring.play()

        val vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(VibrationEffect.createOneShot(1500, VibrationEffect.DEFAULT_AMPLITUDE))
    }

}