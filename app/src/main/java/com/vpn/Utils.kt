package com.vpn

import android.content.Context
import android.content.res.Resources
import android.net.wifi.WifiManager
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.vpn.db.viewmodel.RuleViewModel


val Int.px: Int get() = (this * Resources.getSystem().displayMetrics.density).toInt()

fun isWifiActive(context: Context): Boolean {
    val wm = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
    return wm.isWifiEnabled
}

fun toast(text: String, context: Context = MyApp.instance, length: Int = Toast.LENGTH_SHORT) {
    Handler(Looper.getMainLooper()).post { Toast.makeText(context, text, length).show() }
}

var model0: RuleViewModel? = null