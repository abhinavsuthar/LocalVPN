package com.vpn.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.vpn.R
import com.vpn.db.model.AddressFilter
import com.vpn.db.viewmodel.IPViewModel
import com.vpn.ui.adapter.EventCategoryItemDecoration
import com.vpn.ui.adapter.IPFilterListAdapter
import kotlinx.android.synthetic.main.fragment_ipfilter.*


class IPFilterFragment : Fragment() {

    private val adapter by lazy { IPFilterListAdapter(this, model) }
    private val model by lazy { ViewModelProviders.of(this).get(IPViewModel::class.java) }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_ipfilter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        initRecyclerView()

        add_filter.setOnClickListener {
            if (filter_text.text.isNullOrBlank()) return@setOnClickListener
            val url = filter_text.text?.toString() ?: ""
            model.addFilter(AddressFilter(System.currentTimeMillis().toInt(), url))
            filter_text.setText("")
        }
    }

    private fun initRecyclerView() {
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(context, 1)
        recyclerView.addItemDecoration(EventCategoryItemDecoration(8))
    }

    private fun init() {
        model.filters.observe(this, Observer {
            adapter.setList(it)
        })
    }
}
