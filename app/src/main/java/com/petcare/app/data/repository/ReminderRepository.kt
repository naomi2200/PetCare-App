package com.petcare.app.data.repository

import com.petcare.app.data.local.dao.ReminderDao
import com.petcare.app.data.local.entity.ReminderEntity
import kotlinx.coroutines.flow.Flow

class ReminderRepository(
    private val reminderDao: ReminderDao
) {
    fun getRemindersByPet(petId: Int): Flow<List<ReminderEntity>> {
        return reminderDao.getRemindersByPet(petId)
    }

    fun getAllReminders(): Flow<List<ReminderEntity>> {
        return reminderDao.getAllReminders()
    }

    suspend fun getReminderById(reminderId: Int): ReminderEntity? {
        return reminderDao.getReminderById(reminderId)
    }

    suspend fun insertReminder(reminder: ReminderEntity): Long {
        return reminderDao.insertReminder(reminder)
    }

    suspend fun updateReminder(reminder: ReminderEntity) {
        reminderDao.updateReminder(reminder)
    }

    suspend fun deleteReminder(reminder: ReminderEntity) {
        reminderDao.deleteReminder(reminder)
    }
}
