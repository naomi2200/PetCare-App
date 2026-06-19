package com.petcare.app.data.local.dao

import androidx.room.*
import com.petcare.app.data.local.entity.PetEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PetDao {

    @Query("SELECT * FROM pets WHERE userId = :userId ORDER BY nombre ASC")
    fun getPetsByUser(userId: String): Flow<List<PetEntity>>

    @Query("SELECT * FROM pets WHERE id = :petId LIMIT 1")
    suspend fun getPetById(petId: Int): PetEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPet(pet: PetEntity)

    @Update
    suspend fun updatePet(pet: PetEntity)

    @Delete
    suspend fun deletePet(pet: PetEntity)
}