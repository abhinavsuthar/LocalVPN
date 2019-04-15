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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.vpn.R
import com.vpn.db.viewmodel.RuleViewModel
import com.vpn.model0
import com.vpn.services.InternetAccessVpnService
import com.vpn.ui.adapter.AppListAdapter
import kotlinx.android.synthetic.main.fragment_app_internet_access.*


class AppInternetAccessFragment : Fragment() {

    private val vpnServiceRequestCode = 100
    private val adapter by lazy { AppListAdapter(model) }
    private val model by lazy { ViewModelProviders.of(this).get(RuleViewModel::class.java) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_app_internet_access, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        model0 = model
        setUpRecyclerView()

        model.rules.observe(this, Observer {
            adapter.setData(it)
            prepare()
            progressBar.hide()
        })
    }

    private fun setUpRecyclerView() {
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(context, 1)
    }

    private fun startService() {
        val intent = Intent(context, InternetAccessVpnService::class.java)
        context?.startService(intent)
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
            startService()
        }
    }
}
