package com.vpn.db.repository

import com.vpn.db.model.AddressFilter
import com.vpn.db.room.db
import io.reactivex.Observable

class IPFilterRepository {

    private val dao by lazy { db.addressFilterDao() }

    fun getFilters(): Observable<List<AddressFilter>> {
        return dao.get()
    }

    fun addFilter(filter: AddressFilter) {
        dao.add(filter)
    }

    fun deleteFilter(filter: AddressFilter) {
        dao.delete(filter.id)
    }
}