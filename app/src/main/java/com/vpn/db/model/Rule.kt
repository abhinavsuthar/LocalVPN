package com.vpn.db.model

import android.content.pm.PackageInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Rule(
    val info: PackageInfo?,
    @PrimaryKey
    val name: String,
    val system: Boolean? = false,
    val disabled: Boolean? = false,
    val wifi_blocked: Boolean? = false,
    val other_blocked: Boolean? = false,
    val changed: Boolean? = false
) : Serializable