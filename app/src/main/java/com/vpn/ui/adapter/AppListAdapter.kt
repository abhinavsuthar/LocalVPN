package com.vpn.ui.adapter

import android.content.Context
import android.content.pm.PackageInfo
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vpn.MyApp
import com.vpn.R
import com.vpn.db.model.Rule
import com.vpn.db.viewmodel.RuleViewModel


class AppListAdapter(private val model: RuleViewModel) : RecyclerView.Adapter<AppListAdapter.MyViewHolder>() {

    private val list: ArrayList<Rule> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.app_card, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindDate(position)
    }

    fun setData(list: List<Rule>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val name: TextView = view.findViewById(R.id.name)
        private val icon: ImageView = view.findViewById(R.id.icon)
        private val mobile: ImageView = view.findViewById(R.id.mobile)
        private val wifi: ImageView = view.findViewById(R.id.wifi)

        fun bindDate(position: Int) {

            val rule = list[position]
            name.text = rule.name
            if (rule.info != null) icon.setImageDrawable(getIcon(rule.info))

            if (rule.other_blocked == true) mobile.setImageResource(R.drawable.mobile_off)
            else mobile.setImageResource(R.drawable.mobile_on)

            mobile.setOnClickListener {
                val x = Rule(
                    rule.info,
                    rule.name,
                    rule.system,
                    rule.disabled,
                    rule.wifi_blocked,
                    rule.other_blocked != true,
                    rule.changed
                )
                model.changeRule(x)
            }

            if (rule.wifi_blocked == true) wifi.setImageResource(R.drawable.wifi_off)
            else wifi.setImageResource(R.drawable.wifi_on)

            wifi.setOnClickListener {
                val x = Rule(
                    rule.info,
                    rule.name,
                    rule.system,
                    rule.disabled,
                    rule.wifi_blocked != true,
                    rule.other_blocked,
                    rule.changed
                )
                model.changeRule(x)
            }
        }

        private fun getIcon(info: PackageInfo, context: Context = MyApp.instance): Drawable {
            return info.applicationInfo.loadIcon(context.packageManager)
        }
    }
}