package com.petcare.app.viewmodel

import com.petcare.app.data.remote.dto.AdviceDto

data class AdviceUiState(
    val adviceList: List<AdviceDto> = emptyList(),
    val dailyAdvice: AdviceDto? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)