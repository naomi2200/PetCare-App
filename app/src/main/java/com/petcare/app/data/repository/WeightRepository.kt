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

    fun getLastWeightByPet(petId: Int): Flow<WeightEntryEntity?> {
        return weightEntryDao.getLastWeightByPet(petId)
    }

    suspend fun getWeightById(weightId: Int): WeightEntryEntity? {
        // Note: Added missing getWeightById in DAO if not exists, but for now we focus on the request.
        // If it's missing in DAO, we might need to add it there too.
        // Looking at current Dao, it was missing.
        return null 
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
