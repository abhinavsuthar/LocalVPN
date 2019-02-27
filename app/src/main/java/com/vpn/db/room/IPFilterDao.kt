package com.vpn.db.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vpn.db.model.IPFilter
import io.reactivex.Observable

@Dao
abstract class IPFilterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun add(filter: IPFilter)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun add(filters: List<IPFilter>)

    @Query("SELECT * FROM IPFilter")
    abstract fun get(): Observable<List<IPFilter>>

    @Query("DELETE FROM IPFilter")
    abstract fun deleteAll()

    @Query("DELETE FROM IPFilter WHERE ip = :ip")
    abstract fun delete(ip: String)
}