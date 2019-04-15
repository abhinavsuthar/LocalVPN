package com.vpn.db.viewmodel

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vpn.MyApp
import com.vpn.db.model.AddressFilter
import com.vpn.db.repository.IPFilterRepository
import io.reactivex.disposables.Disposable

class IPViewModel : ViewModel() {

    private val repo by lazy { IPFilterRepository() }
    val filters: MutableLiveData<List<AddressFilter>> = MutableLiveData()

    init {
        readFilters()
    }

    private fun readFilters(): Disposable? {
        return repo.getFilters().subscribe({
            filters.postValue(it)
        }, {
            Toast.makeText(MyApp.instance, "Error getting new messages", Toast.LENGTH_SHORT).show()
        })
    }

    fun addFilter(filter: AddressFilter){
        repo.addFilter(filter)
    }

    fun deleteFilter(filter: AddressFilter){
        repo.deleteFilter(filter)
    }


}