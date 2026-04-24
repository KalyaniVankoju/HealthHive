package com.example.rtrpproject

import androidx.lifecycle.LiveData

class WaterRepository(private val waterDao: WaterDao) {

    val allWaterEntries: LiveData<List<Water>> =
        waterDao.getAllWaterEntries()

    suspend fun insertWater(water: Water) {
        waterDao.insertWater(water)
    }

    fun getTodayWaterEntries(todayDate: String): LiveData<List<Water>> {
        return waterDao.getTodayWaterEntries(todayDate)
    }

    // 🔥 ADD THIS (weekly analytics)
    fun getLast7DaysWater(startDate: String): LiveData<List<Water>> {
        return waterDao.getLast7DaysWater(startDate)
    }
}