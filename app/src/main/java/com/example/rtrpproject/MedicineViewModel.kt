package com.example.rtrpproject

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MedicineViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: MedicineRepository
    val allMedicines: LiveData<List<Medicine>>

    init {
        val medicineDao = AppDatabase.getDatabase(application).medicineDao()
        repository = MedicineRepository(medicineDao)
        allMedicines = repository.allMedicines
    }

    fun insertMedicine(medicine: Medicine) {
        viewModelScope.launch {
            repository.insertMedicine(medicine)
        }
    }

    fun deleteMedicine(medicine: Medicine) {
        viewModelScope.launch {
            repository.deleteMedicine(medicine)
        }
    }
}