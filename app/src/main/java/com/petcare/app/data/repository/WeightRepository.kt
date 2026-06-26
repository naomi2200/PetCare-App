package com.petcare.app.data.repository

import com.petcare.app.data.local.dao.WeightEntryDao
import com.petcare.app.data.local.entity.WeightEntryEntity
import kotlinx.coroutines.flow.Flow

class WeightRepository(
    private val weightEntryDao: WeightEntryDao
) {
    fun getWeightsByPet(petId: Int): Flow<List<WeightEntryEntity>> {
        return weightEntryDao.getWeightsByPet(petId)
    }

    suspend fun getWeightById(weightId: Int): WeightEntryEntity? {
        return weightEntryDao.getWeightById(weightId)
    }

    suspend fun insertWeight(weight: WeightEntryEntity) {
        weightEntryDao.insertWeight(weight)
    }

    suspend fun updateWeight(weight: WeightEntryEntity) {
        weightEntryDao.updateWeight(weight)
    }

    suspend fun deleteWeight(weight: WeightEntryEntity) {
        weightEntryDao.deleteWeight(weight)
    }
}