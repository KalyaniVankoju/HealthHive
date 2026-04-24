package com.example.rtrpproject

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface StepDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateStep(stepEntry: StepEntry)

    @Query("SELECT * FROM step_table ORDER BY date DESC")
    fun getAllStepEntries(): LiveData<List<StepEntry>>

    @Query("SELECT * FROM step_table WHERE date = :todayDate")
    fun getTodayStepEntries(todayDate: String): LiveData<List<StepEntry>>

    @Query("SELECT * FROM step_table WHERE date = :todayDate LIMIT 1")
    suspend fun getStepEntryByDate(todayDate: String): StepEntry?

    @Query("SELECT * FROM step_table ORDER BY date DESC LIMIT 7")
    fun getLast7DaysSteps(): LiveData<List<StepEntry>>
}