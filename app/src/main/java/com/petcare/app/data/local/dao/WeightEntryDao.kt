package com.petcare.app.data.local.dao

import androidx.room.*
import com.petcare.app.data.local.entity.WeightEntryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WeightEntryDao {
    @Query("SELECT * FROM weight_entries WHERE petId = :petId ORDER BY date DESC")
    fun getWeightsByPet(petId: Int): Flow<List<WeightEntryEntity>>

    @Query("SELECT * FROM weight_entries WHERE petId = :petId ORDER BY date DESC LIMIT 1")
    fun getLastWeightByPet(petId: Int): Flow<WeightEntryEntity?>

    @Query("SELECT * FROM weight_entries WHERE id = :weightId LIMIT 1")
    suspend fun getWeightById(weightId: Int): WeightEntryEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeight(weight: WeightEntryEntity)

    @Update
    suspend fun updateWeight(weight: WeightEntryEntity)

    @Delete
    suspend fun deleteWeight(weight: WeightEntryEntity)
}
