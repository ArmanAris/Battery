package com.aris.batterymanager.utils

import android.content.Context
import android.content.SharedPreferences

class spManager {

    companion object {
        private var sharedPreferences: SharedPreferences? = null
        private var editor: SharedPreferences.Editor? = null
        private val spb = "SharedPreferencesBoolean"
        private val isServiceOn = "isServiceOn"

        fun isServiceOn(context: Context): Boolean? {
            sharedPreferences = context.getSharedPreferences(spb, Context.MODE_PRIVATE)
            return sharedPreferences?.getBoolean(isServiceOn, false)
            //getInt , getString , getBoolean , ...
        }

        fun setServiceState(context: Context, boolean: Boolean?) {
            sharedPreferences = context.getSharedPreferences(spb, Context.MODE_PRIVATE)
            editor = sharedPreferences?.edit()
            editor?.putBoolean(isServiceOn, boolean!!)
            editor?.apply()
        }
    }

}