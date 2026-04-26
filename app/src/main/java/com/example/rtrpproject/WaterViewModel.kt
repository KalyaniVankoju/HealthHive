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

    fun insertDummyWaterOnce() {
        viewModelScope.launch {
            val existing = repository.allWaterEntries.value ?: emptyList()

            if (existing.isNotEmpty()) {
                return@launch
            }

            val dates = getLast7Days()

            val dummyWater = listOf(
                1500, 2200, 1800, 3000, 2500, 2000, 2800
            )

            for (i in dates.indices) {
                repository.insertWater(
                    Water(
                        amount = dummyWater[i],
                        date = dates[i]
                    )
                )
            }
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