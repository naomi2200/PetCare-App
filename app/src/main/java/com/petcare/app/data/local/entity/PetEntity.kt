package com.petcare.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pets")
data class PetEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val nombre: String,
    val especie: String,
    val raza: String?,
    val sexo: String,
    val edad: Int,
    val peso: Double,
    val fechaNacimiento: String?,
    val fotoUrl: String?,
    val userId: String
)