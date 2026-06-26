package com.petcare.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.petcare.app.data.repository.AdviceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AdviceViewModel(
    private val repository: AdviceRepository = AdviceRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdviceUiState())
    val uiState: StateFlow<AdviceUiState> = _uiState.asStateFlow()

    fun loadAdvice() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val advice = repository.getAdvice()
                _uiState.update { it.copy(adviceList = advice, isLoading = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(
                    errorMessage = e.message ?: "No se pudieron cargar los consejos.",
                    isLoading = false
                ) }
            }
        }
    }

    fun loadRandomAdvice() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val advice = repository.getRandomAdvice()
                _uiState.update { it.copy(dailyAdvice = advice, isLoading = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = e.message, isLoading = false) }
            }
        }
    }

    // Haz lo mismo para las funciones por especie si las usas:
    fun loadAdviceBySpecies(species: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val advice = repository.getAdviceBySpecies(species)
                _uiState.update { it.copy(adviceList = advice, isLoading = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = e.message, isLoading = false) }
            }
        }
    }

    fun loadRandomAdviceBySpecies(species: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val advice = repository.getRandomAdviceBySpecies(species)
                _uiState.update { it.copy(dailyAdvice = advice, isLoading = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = e.message, isLoading = false) }
            }
        }
    }
}