package com.example.rtrpproject

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface WaterDao {

    @Insert
    suspend fun insertWater(water: Water)

    @Query("SELECT * FROM water_table ORDER BY date DESC, id DESC")
    fun getAllWaterEntries(): LiveData<List<Water>>

    @Query("SELECT * FROM water_table WHERE date = :todayDate ORDER BY id DESC")
    fun getTodayWaterEntries(todayDate: String): LiveData<List<Water>>

    @Query("SELECT * FROM water_table WHERE date >= :startDate ORDER BY date DESC, id DESC")
    fun getLast7DaysWater(startDate: String): LiveData<List<Water>>
}