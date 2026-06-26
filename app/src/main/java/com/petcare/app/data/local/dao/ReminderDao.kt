package com.petcare.app.data.local.dao

import androidx.room.*
import com.petcare.app.data.local.entity.ReminderEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReminderDao {

    @Query("SELECT * FROM reminders WHERE petId = :petId ORDER BY reminderDate ASC")
    fun getRemindersByPet(petId: Int): Flow<List<ReminderEntity>>

    @Query("SELECT * FROM reminders ORDER BY reminderDate ASC")
    fun getAllReminders(): Flow<List<ReminderEntity>>

    @Query("SELECT * FROM reminders WHERE id = :reminderId LIMIT 1")
    suspend fun getReminderById(reminderId: Int): ReminderEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReminder(reminder: ReminderEntity)

    @Update
    suspend fun updateReminder(reminder: ReminderEntity)

    @Delete
    suspend fun deleteReminder(reminder: ReminderEntity)
}