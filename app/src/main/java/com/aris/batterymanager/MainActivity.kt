package com.aris.batterymanager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.aris.batterymanager.databinding.ActivityBatteryBinding
import com.aris.batterymanager.databinding.ActivityMainBinding
import java.util.Timer
import kotlin.concurrent.timerTask

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val textArray = arrayOf(
            "  باتری خود را قدرتمند کنید",
            "   باتری خود را ایمن کنید",
            "باتری گوشی خود را مدیریت کنید", )

        for (i in 1..3) {
            text(textArray[i - 1], (i * 1000).toLong())
        }

        Timer().schedule(timerTask {
            startActivity(Intent(this@MainActivity, BatteryActivity::class.java))
            finish()
        }, 3000)

    }

    fun text(text: String, time: Long) {
        Timer().schedule(timerTask {
            runOnUiThread({ binding.textView.text = text })
        }, time)
    }

}