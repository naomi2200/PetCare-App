package com.petcare.app.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.petcare.app.data.local.dao.MedicalRecordDao
import com.petcare.app.data.local.dao.PetDao
import com.petcare.app.data.local.dao.WeightEntryDao
import com.petcare.app.data.local.dao.ReminderDao
import com.petcare.app.data.local.entity.MedicalRecordEntity
import com.petcare.app.data.local.entity.PetEntity
import com.petcare.app.data.local.entity.WeightEntryEntity
import com.petcare.app.data.local.entity.ReminderEntity

@Database(
    entities = [
        PetEntity::class,
        MedicalRecordEntity::class,
        WeightEntryEntity::class,
        ReminderEntity::class
    ],
    version = 5,
    exportSchema = false
)
abstract class PetCareDatabase : RoomDatabase() {

    abstract fun petDao(): PetDao
    abstract fun medicalRecordDao(): MedicalRecordDao

    abstract fun weightEntryDao(): WeightEntryDao

    abstract fun reminderDao(): ReminderDao

    companion object {
        @Volatile
        private var INSTANCE: PetCareDatabase? = null

        fun getDatabase(context: Context): PetCareDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PetCareDatabase::class.java,
                    "petcare_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}