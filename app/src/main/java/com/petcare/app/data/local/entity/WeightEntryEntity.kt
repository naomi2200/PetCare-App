package com.petcare.app.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "weight_entries",
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
data class WeightEntryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val petId: Int,
    val weight: Double,
    val date: String,
    val notes: String? = null
)