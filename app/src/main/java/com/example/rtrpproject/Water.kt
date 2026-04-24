package com.example.rtrpproject

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "water_table")
data class Water(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val amount: Int,
    val date:String
)