package com.petcare.app.data.local.dao

import androidx.room.*
import com.petcare.app.data.local.entity.MedicalRecordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MedicalRecordDao {

    @Query("SELECT * FROM medical_records WHERE petId = :petId ORDER BY date DESC")
    fun getRecordsByPet(petId: Int): Flow<List<MedicalRecordEntity>>

    @Query("SELECT COUNT(*) FROM medical_records JOIN pets ON medical_records.petId = pets.id WHERE pets.userId = :userId")
    fun getTotalRecordsCount(userId: String): Flow<Int>

    @Query("SELECT * FROM medical_records WHERE id = :recordId LIMIT 1")
    suspend fun getRecordById(recordId: Int): MedicalRecordEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecord(record: MedicalRecordEntity)

    @Update
    suspend fun updateRecord(record: MedicalRecordEntity)

    @Delete
    suspend fun deleteRecord(record: MedicalRecordEntity)
}