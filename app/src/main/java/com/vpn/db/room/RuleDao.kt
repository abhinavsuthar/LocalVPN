package com.vpn.db.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vpn.db.model.Rule
import io.reactivex.Observable

@Dao
abstract class RuleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun addRule(rule: Rule)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun addRules(rules: List<Rule>)

    @Query("SELECT * FROM RULE")
    abstract fun getRules(): Observable<List<Rule>>

    @Query("DELETE FROM RULE")
    abstract fun deleteAllRules()

    @Query("DELETE FROM RULE WHERE name = :name")
    abstract fun deleteRule(name: String)

    @Query("SELECT * FROM RULE WHERE name = :name")
    abstract fun getRule(name: String): Rule
}