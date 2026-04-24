package com.example.rtrpproject

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MedicineDao {

    @Insert
    suspend fun insertMedicine(medicine: Medicine)

    @Query("SELECT * FROM medicine_table ORDER BY id DESC")
    fun getAllMedicines(): LiveData<List<Medicine>>

    @Delete
    suspend fun deleteMedicine(medicine: Medicine)
}