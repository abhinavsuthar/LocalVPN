package com.vpn.db.repository

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.preference.PreferenceManager
import com.vpn.MyApp
import com.vpn.db.model.Rule
import com.vpn.db.room.db
import java.util.*


class RulesRepository {

    private val dao by lazy { db.ruleDao() }
    private val context by lazy { MyApp.instance }
    private val prefs by lazy { PreferenceManager.getDefaultSharedPreferences(context) }
    private val wifi by lazy { context.getSharedPreferences("wifi", Context.MODE_PRIVATE) }
    private val other by lazy { context.getSharedPreferences("other", Context.MODE_PRIVATE) }

    // fun getRules(): Observable<List<Rule>> = dao.getRules()
    fun getRule(name: String): Rule? = dao.getRule(name)

    fun addRules(list: ArrayList<Rule>) {
        dao.addRules(list)
    }

    fun getRules(): ArrayList<Rule> {
        val pm = context.packageManager

        val wlWifi = prefs.getBoolean("whitelist_wifi", true)
        val wlOther = prefs.getBoolean("whitelist_other", true)

        val list = ArrayList<Rule>()
        for (info in context.packageManager.getInstalledPackages(PackageManager.GET_ACTIVITIES)) {
            val name = info.applicationInfo.loadLabel(pm).toString()
            val system = info.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM !== 0
            // if (system) continue
            val blWifi = wifi.getBoolean(info.packageName, wlWifi)
            val blOther = other.getBoolean(info.packageName, wlOther)
            val changed = blWifi != wlWifi || blOther != wlOther
            list.add(Rule(info, name, system, false, blWifi, blOther, changed))
        }

        list.sortBy { it.name }
        return list
    }

    fun changeRule(rule: Rule) {
        wifi.edit().putBoolean(rule.info?.packageName, rule.wifi_blocked ?: false).apply()
        other.edit().putBoolean(rule.info?.packageName, rule.other_blocked ?: false).apply()
    }
}