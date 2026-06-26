package com.petcare.app.viewmodel

import com.petcare.app.data.local.entity.MedicalRecordEntity

data class MedicalUiState(
    val records: List<MedicalRecordEntity> = emptyList(),
    val selectedRecord: MedicalRecordEntity? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)