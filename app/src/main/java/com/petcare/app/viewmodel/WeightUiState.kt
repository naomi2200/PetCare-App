package com.petcare.app.viewmodel

import com.petcare.app.data.local.entity.WeightEntryEntity

data class WeightUiState(
    val weights: List<WeightEntryEntity> = emptyList(),
    val lastWeight: WeightEntryEntity? = null,
    val selectedWeight: WeightEntryEntity? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)