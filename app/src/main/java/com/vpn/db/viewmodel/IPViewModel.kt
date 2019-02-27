package com.vpn.db.viewmodel

import androidx.lifecycle.ViewModel
import com.vpn.db.repository.IPFilterRepository

class IPViewModel : ViewModel() {

    private val repo by lazy { IPFilterRepository() }
}