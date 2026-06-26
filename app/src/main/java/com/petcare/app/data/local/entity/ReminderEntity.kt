package com.petcare.app.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "reminders",
    foreignKeys = [
        ForeignKey(
            entity = PetEntity::class,
            parentColumns = ["id"],
            childColumns = ["petId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["petId"])]
)
data class ReminderEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val petId: Int,
    val title: String,
    val description: String? = null,
    val reminderType: String,
    val reminderDate: String,
    val reminderTime: String? = null,
    val isCompleted: Boolean = false,
    val anticipationMinutes: Int = 0
)