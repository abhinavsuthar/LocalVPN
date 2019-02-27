package com.vpn.db.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vpn.db.model.Rule
import com.vpn.db.repository.RulesRepository
import org.jetbrains.anko.doAsync


class RuleViewModel : ViewModel() {

    private val repo by lazy { RulesRepository() }
    val rules: MutableLiveData<List<Rule>> = MutableLiveData()

    init {
        doAsync { getRules() }
    }

    private fun getRules() {
        rules.postValue(repo.getRules())
    }

    fun changeRule(rule: Rule) {
        repo.changeRule(rule)
        doAsync { getRules() }
    }
}