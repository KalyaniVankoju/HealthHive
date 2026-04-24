package com.example.rtrpproject

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.util.Calendar

class WaterViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: WaterRepository
    val allWaterEntries: LiveData<List<Water>>

    init {
        val waterDao = AppDatabase.getDatabase(application).waterDao()
        repository = WaterRepository(waterDao)
        allWaterEntries = repository.allWaterEntries
    }

    fun insertWater(amount: Int) {
        viewModelScope.launch {
            repository.insertWater(
                Water(
                    amount = amount,
                    date = getTodayDate()
                )
            )
        }
    }

    fun getTodayWaterEntries(todayDate: String): LiveData<List<Water>> {
        return repository.getTodayWaterEntries(todayDate)
    }

    fun getLast7DaysWater(): LiveData<List<Water>> {
        return repository.getLast7DaysWater(getDate7DaysAgo())
    }
}

fun getDate7DaysAgo(): String {
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.DAY_OF_MONTH, -6)
    return String.format(
        "%04d-%02d-%02d",
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH) + 1,
        calendar.get(Calendar.DAY_OF_MONTH)
    )
}