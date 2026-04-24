package com.example.rtrpproject

import androidx.lifecycle.LiveData

class MedicineRepository(private val medicineDao: MedicineDao) {

    val allMedicines: LiveData<List<Medicine>> = medicineDao.getAllMedicines()

    suspend fun insertMedicine(medicine: Medicine) {
        medicineDao.insertMedicine(medicine)
    }

    suspend fun deleteMedicine(medicine: Medicine) {
        medicineDao.deleteMedicine(medicine)
    }
}