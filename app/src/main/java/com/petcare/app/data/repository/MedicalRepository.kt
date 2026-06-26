package com.petcare.app.data.repository

import com.petcare.app.data.local.dao.MedicalRecordDao
import com.petcare.app.data.local.entity.MedicalRecordEntity
import kotlinx.coroutines.flow.Flow

class MedicalRepository(
    private val medicalRecordDao: MedicalRecordDao
) {
    fun getRecordsByPet(petId: Int): Flow<List<MedicalRecordEntity>> {
        return medicalRecordDao.getRecordsByPet(petId)
    }

    suspend fun getRecordById(recordId: Int): MedicalRecordEntity? {
        return medicalRecordDao.getRecordById(recordId)
    }

    suspend fun insertRecord(record: MedicalRecordEntity) {
        medicalRecordDao.insertRecord(record)
    }

    suspend fun updateRecord(record: MedicalRecordEntity) {
        medicalRecordDao.updateRecord(record)
    }

    suspend fun deleteRecord(record: MedicalRecordEntity) {
        medicalRecordDao.deleteRecord(record)
    }
}