package com.vpn.db.room

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.vpn.MyApp
import com.vpn.db.converters.RuleConverter
import com.vpn.db.model.AddressFilter
import com.vpn.db.model.IPFilter
import com.vpn.db.model.Rule

@Database(entities = [IPFilter::class, Rule::class, AddressFilter::class], version = 1, exportSchema = false)
@TypeConverters(RuleConverter::class)
abstract class MyDatabase : RoomDatabase() {

    @TypeConverters(RuleConverter::class)
    abstract fun ruleDao(): RuleDao

    abstract fun iPFilterDao(): IPFilterDao

    abstract fun addressFilterDao(): AddressFilterDao
}


val db = Room.databaseBuilder(MyApp.instance, MyDatabase::class.java, "database")
    .allowMainThreadQueries()
    .fallbackToDestructiveMigration()
    .build()