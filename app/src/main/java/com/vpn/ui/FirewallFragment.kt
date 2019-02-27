package com.vpn.ui


import android.app.Activity
import android.content.Intent
import android.net.VpnService
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.vpn.R
import com.vpn.services.MyVpnService
import kotlinx.android.synthetic.main.fragment_firewall.*


class FirewallFragment : Fragment() {

    private val vpnServiceRequestCode = 100

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_firewall, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        startVPN.setOnClickListener {
            prepare()
            startVPN.isEnabled = false
        }
    }

    private fun prepare() {
        val intent: Intent? = VpnService.prepare(requireActivity())
        if (intent != null) startActivityForResult(intent, vpnServiceRequestCode)
        else onActivityResult(vpnServiceRequestCode, Activity.RESULT_OK, null)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        Log.d("suthar", "onActivityResult, $requestCode, $resultCode")

        if (requestCode == vpnServiceRequestCode && resultCode == Activity.RESULT_OK) {
            Log.d("suthar", "ok")
            val intent = Intent(context, MyVpnService::class.java)
            context?.startService(intent)
        }
    }
}
