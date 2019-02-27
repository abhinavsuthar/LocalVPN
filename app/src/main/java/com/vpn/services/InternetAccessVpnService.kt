package com.vpn.services

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.net.VpnService
import android.os.ParcelFileDescriptor
import android.util.Log
import com.vpn.R
import com.vpn.isWifiActive
import com.vpn.model0
import com.vpn.toast
import com.vpn.ui.MainActivity


class InternetAccessVpnService : VpnService() {

    private val tag = "suthar"
    private val model by lazy { model0 }

    override fun onCreate() {
        super.onCreate()

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        vpnStart()
        return Service.START_STICKY
    }

    private fun vpnStart(): ParcelFileDescriptor? {

        Log.i(tag, "Starting")

        // Check if Wi-Fi
        val wifi = isWifiActive(this)
        Log.i(tag, "wifi=$wifi")

        // Build VPN service
        val builder = Builder()
        builder.setSession(getString(R.string.app_name))
        builder.addAddress("10.1.10.1", 32)
        builder.addAddress("fd00:1:fd00:1:fd00:1:fd00:1", 128)
        builder.addRoute("0.0.0.0", 0)
        builder.addRoute("0:0:0:0:0:0:0:0", 0)

        // Build configure intent
        val configure = Intent(this, MainActivity::class.java)
        val pi = PendingIntent.getActivity(this, 0, configure, PendingIntent.FLAG_UPDATE_CURRENT)
        builder.setConfigureIntent(pi)

        model?.rules?.observeForever {
            // Add list of allowed applications
            for (rule in it)
                if ((if (wifi) rule.wifi_blocked else rule.other_blocked) != true) {
                    Log.i(tag, "Allowing " + rule.info?.packageName)
                    try {
                        builder.addDisallowedApplication(rule.info?.packageName)
                    } catch (ex: PackageManager.NameNotFoundException) {
                        Log.e(tag, ex.toString() + "\n" + Log.getStackTraceString(ex))
                    }

                }
        }


        return try {
            builder.establish()
        } catch (ex: Throwable) {
            Log.e(tag, ex.toString() + "\n" + Log.getStackTraceString(ex))
            toast(ex.toString())
            null
        }

    }
}