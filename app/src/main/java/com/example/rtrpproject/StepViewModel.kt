package com.example.rtrpproject

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class StepViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: StepRepository
    val allStepEntries: LiveData<List<StepEntry>>

    init {
        val stepDao = AppDatabase.getDatabase(application).stepDao()
        repository = StepRepository(stepDao)
        allStepEntries = repository.allStepEntries
    }

    fun insertSteps(stepsToAdd: Int) {
        val today = getTodayDate()
        viewModelScope.launch {
            val existingEntry = repository.getStepEntryByDate(today)
            if (existingEntry != null) {
                val updatedEntry = existingEntry.copy(
                    steps = existingEntry.steps + stepsToAdd
                )
                repository.insertOrUpdateStep(updatedEntry)
            } else {
                repository.insertOrUpdateStep(
                    StepEntry(
                        date = today,
                        steps = stepsToAdd
                    )
                )
            }
        }
    }

    fun incrementAutoStep() {
        val today = getTodayDate()
        viewModelScope.launch {
            val existingEntry = repository.getStepEntryByDate(today)
            if (existingEntry != null) {
                val updatedEntry = existingEntry.copy(
                    steps = existingEntry.steps + 1
                )
                repository.insertOrUpdateStep(updatedEntry)
            } else {
                repository.insertOrUpdateStep(
                    StepEntry(
                        date = today,
                        steps = 1
                    )
                )
            }
        }
    }

    fun getTodayStepEntries(todayDate: String): LiveData<List<StepEntry>> {
        return repository.getTodayStepEntries(todayDate)
    }

    // 🔥 ADD THIS (weekly analytics)
    fun getLast7DaysSteps(): LiveData<List<StepEntry>> {
        return repository.getLast7DaysSteps()
    }
}