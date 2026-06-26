package com.petcare.app

import android.app.Application
import com.petcare.app.data.local.database.PetCareDatabase
import com.petcare.app.data.repository.PetRepository
import com.petcare.app.data.repository.MedicalRepository
import com.petcare.app.data.repository.WeightRepository
import com.petcare.app.data.repository.ReminderRepository

class PetCareApplication : Application() {

    val database by lazy {
        PetCareDatabase.getDatabase(this)
    }

    val petRepository by lazy {
        PetRepository(database.petDao())
    }

    val medicalRepository by lazy {
        MedicalRepository(database.medicalRecordDao())
    }

    val weightRepository by lazy {
        WeightRepository(database.weightEntryDao())
    }

    val reminderRepository by lazy {
        ReminderRepository(database.reminderDao())
    }
}