package com.jodi.companioncompatibility.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "jodi")
data class JodiDemoTable(
    @PrimaryKey
    @ColumnInfo(name = "demo")
    val demo: String
)
