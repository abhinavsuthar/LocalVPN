package com.vpn.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class IPFilter(
    @PrimaryKey
    val ip: String
)