package com.petcare.app.data.repository

import com.petcare.app.data.local.dao.PetDao
import com.petcare.app.data.local.entity.PetEntity
import kotlinx.coroutines.flow.Flow

class PetRepository(
    private val petDao: PetDao
) {
    fun getPetsByUser(userId: String): Flow<List<PetEntity>> {
        return petDao.getPetsByUser(userId)
    }

    suspend fun getPetById(petId: Int): PetEntity? {
        return petDao.getPetById(petId)
    }

    suspend fun insertPet(pet: PetEntity) {
        petDao.insertPet(pet)
    }

    suspend fun updatePet(pet: PetEntity) {
        petDao.updatePet(pet)
    }

    suspend fun deletePet(pet: PetEntity) {
        petDao.deletePet(pet)
    }
}