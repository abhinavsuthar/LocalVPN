package com.vpn.db.converters

import android.content.pm.PackageInfo
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class RuleConverter {
    @TypeConverter
    fun fromString(value: String): PackageInfo {
        val objectType = object : TypeToken<PackageInfo>() {}.type
        return Gson().fromJson(value, objectType)
    }

    @TypeConverter
    fun fromInfo(info: PackageInfo): String {
        val gson = Gson()
        return gson.toJson(info)
    }
}

