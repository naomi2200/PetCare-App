package com.petcare.app

import android.app.Application
import com.petcare.app.data.local.database.PetCareDatabase
import com.petcare.app.data.repository.PetRepository
import com.petcare.app.data.repository.MedicalRepository

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
}