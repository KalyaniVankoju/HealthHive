package com.example.rtrpproject

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "step_table")
data class StepEntry(
    @PrimaryKey
    val date: String,   // unique per day
    val steps: Int
)