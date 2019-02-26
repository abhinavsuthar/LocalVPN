package com.vpn.ui

import android.app.Activity
import android.content.Intent
import android.net.VpnService
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.vpn.MyVpnService
import com.vpn.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val vpnServiceRequestCode = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startVPN.setOnClickListener { prepare() }
    }

    private fun prepare() {
        val intent: Intent? = VpnService.prepare(this)
        if (intent != null) startActivityForResult(intent, vpnServiceRequestCode)
        else onActivityResult(vpnServiceRequestCode, Activity.RESULT_OK, null)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == vpnServiceRequestCode && resultCode == Activity.RESULT_OK) {
            val intent = Intent(this, MyVpnService::class.java)
            startService(intent)
        }
    }
}
