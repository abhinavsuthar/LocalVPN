package com.vpn.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AddressFilter(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val address: String
)