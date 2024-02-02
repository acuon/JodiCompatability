package com.jodi.companioncompatibility.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [JodiDemoTable::class], version = 1, exportSchema = false)
abstract class JodiDatabase : RoomDatabase() {
    abstract fun getDao(): JodiDao
}