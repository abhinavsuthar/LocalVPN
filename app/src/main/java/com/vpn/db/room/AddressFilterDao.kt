package com.vpn.db.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vpn.db.model.AddressFilter
import io.reactivex.Observable

@Dao
abstract class AddressFilterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun add(filter: AddressFilter)

    @Query("SELECT * FROM AddressFilter")
    abstract fun get(): Observable<List<AddressFilter>>

    @Query("DELETE FROM AddressFilter")
    abstract fun deleteAll()

    @Query("DELETE FROM AddressFilter WHERE id = :id")
    abstract fun delete(id: Int)

    @Query("DELETE FROM AddressFilter WHERE address = :address")
    abstract fun delete(address: String)
}