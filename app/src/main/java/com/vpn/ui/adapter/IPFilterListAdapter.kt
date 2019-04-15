package com.vpn.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.vpn.R
import com.vpn.db.model.AddressFilter
import com.vpn.db.viewmodel.IPViewModel

class IPFilterListAdapter(private val context: Fragment, private val model: IPViewModel) :
    RecyclerView.Adapter<IPFilterListAdapter.MyViewHolder>() {

    private val list: ArrayList<AddressFilter> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.filter_list, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindData(position)
    }

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val text: TextView = view.findViewById(R.id.text)
        private val delete: ImageView = view.findViewById(R.id.delete)

        fun bindData(position: Int) {
            val x = list[position]
            text.text = x.address

            delete.setOnClickListener {
                model.deleteFilter(x)
            }
        }
    }

    fun setList(list: List<AddressFilter>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }
}