package com.example.rtrpproject

import androidx.lifecycle.LiveData

class StepRepository(private val stepDao: StepDao) {

    val allStepEntries: LiveData<List<StepEntry>> =
        stepDao.getAllStepEntries()

    suspend fun insertOrUpdateStep(stepEntry: StepEntry) {
        stepDao.insertOrUpdateStep(stepEntry)
    }

    fun getTodayStepEntries(todayDate: String): LiveData<List<StepEntry>> {
        return stepDao.getTodayStepEntries(todayDate)
    }

    suspend fun getStepEntryByDate(todayDate: String): StepEntry? {
        return stepDao.getStepEntryByDate(todayDate)
    }

    // 🔥 ADD THIS (for weekly analytics)
    fun getLast7DaysSteps(): LiveData<List<StepEntry>> {
        return stepDao.getLast7DaysSteps()
    }
}