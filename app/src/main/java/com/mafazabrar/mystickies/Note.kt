package com.mafazabrar.mystickies

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val title: String,
    val data: String,
    @ColumnInfo(name = "children_count") val childrenCount: Int,
    val children: String) {
}