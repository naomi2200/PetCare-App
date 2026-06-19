package com.petcare.app.viewmodel

import com.petcare.app.data.local.entity.PetEntity

data class PetUiState(
    val pets: List<PetEntity> = emptyList(),
    val selectedPet: PetEntity? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)