package com.aris.batterymanager.adapter

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aris.batterymanager.R
import com.aris.batterymanager.data.model.BatteryModel
import kotlin.math.roundToInt

class BatteryUsageAdapter(
    val context: Context,
    val list: MutableList<BatteryModel>,
    val totalTime: Long,
) :
    RecyclerView.Adapter<BatteryUsageAdapter.ViewHolder>() {

    var finalList: MutableList<BatteryModel>

    init {
        finalList = newList(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.batteryusageadapter, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.appName.text = getName(finalList[position].packageName.toString())
        holder.progressBar.progress = finalList[position].percentUsage
        holder.percent.text = finalList[position].percentUsage.toString() + " %"
        holder.time.text = finalList[position].time
        holder.icon.setImageDrawable(getIcon(finalList[position].packageName.toString()))

    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val icon = itemView.findViewById<ImageView>(R.id.iconApp)
        val time = itemView.findViewById<TextView>(R.id.timeUsage)
        val appName = itemView.findViewById<TextView>(R.id.nameApp)
        val progressBar = itemView.findViewById<ProgressBar>(R.id.progressBar)
        val percent = itemView.findViewById<TextView>(R.id.percentAdapter)
    }

    fun newList(oldList: MutableList<BatteryModel>): MutableList<BatteryModel> {

        val newList: MutableList<BatteryModel> = ArrayList()

        val sortList = oldList.groupBy { it.packageName }
            .mapValues { entry -> entry.value.sumBy { it.percentUsage } }
            .toList()
            .sortedWith(compareBy { it.second }).reversed()

        for (item in sortList) {
            val model = BatteryModel()
            val time = item.second.toFloat() / 100 * totalTime.toFloat() / 1000 / 60
            val hour = time / 60
            val min = time % 60
            Log.e("7171",
                "${item.first} : ${item.second} % And time usage is: ${hour.roundToInt()}:${min.roundToInt()}")
            model.packageName = item.first
            model.percentUsage = item.second
            model.time = "${hour.roundToInt()} Hour ${min.roundToInt()} Minutes"
            newList += model
        }
        return newList
    }

    fun getName(packageName: String): String {
        val packageManager = context.applicationContext.packageManager
        val applicationInfo: ApplicationInfo? = try {
            packageManager.getApplicationInfo(packageName, 0)
        } catch (ex: PackageManager.NameNotFoundException) {
            null
        }
        return if (applicationInfo != null) {
            packageManager.getApplicationLabel(applicationInfo).toString()
        } else {
            "unknown" as String
        }

    }

    fun getIcon(packageName: String): Drawable {
        var icon: Drawable? = null
        try {
            icon = context.packageManager.getApplicationIcon(packageName)
        } catch (ex: PackageManager.NameNotFoundException) {
            ex.printStackTrace()
        }
        return icon!!
    }

}