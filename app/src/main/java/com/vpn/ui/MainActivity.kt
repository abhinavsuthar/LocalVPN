package com.vpn.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.vpn.R

class MainActivity : AppCompatActivity() {

    private val vpnServiceRequestCode = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // findNavController(R.id.nav_host_fragment).navigate(R.id.action_homeFragment_to_appInternetAccessFragment)
    }
}
