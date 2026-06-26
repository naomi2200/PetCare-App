package com.petcare.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.petcare.app.data.repository.AdviceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AdviceViewModel(
    private val repository: AdviceRepository = AdviceRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdviceUiState())
    val uiState: StateFlow<AdviceUiState> = _uiState.asStateFlow()

    fun loadAdvice() {
        viewModelScope.launch {
            _uiState.value = AdviceUiState(isLoading = true)

            try {
                val advice = repository.getAdvice()
                _uiState.value = AdviceUiState(adviceList = advice)
            } catch (e: Exception) {
                _uiState.value = AdviceUiState(
                    errorMessage = e.message ?: "No se pudieron cargar los consejos."
                )
            }
        }
    }

    fun loadAdviceBySpecies(species: String) {
        viewModelScope.launch {
            _uiState.value = AdviceUiState(isLoading = true)

            try {
                val advice = repository.getAdviceBySpecies(species)
                _uiState.value = AdviceUiState(adviceList = advice)
            } catch (e: Exception) {
                _uiState.value = AdviceUiState(
                    errorMessage = e.message ?: "No se pudieron cargar los consejos."
                )
            }
        }
    }

    fun loadRandomAdvice() {
        viewModelScope.launch {
            _uiState.value = AdviceUiState(isLoading = true)

            try {
                val advice = repository.getRandomAdvice()

                _uiState.value = AdviceUiState(
                    dailyAdvice = advice
                )
            } catch (e: Exception) {
                _uiState.value = AdviceUiState(
                    errorMessage = e.message
                )
            }
        }
    }

    fun loadRandomAdviceBySpecies(species: String) {
        viewModelScope.launch {
            _uiState.value = AdviceUiState(isLoading = true)

            try {
                val advice = repository.getRandomAdviceBySpecies(species)

                _uiState.value = AdviceUiState(
                    dailyAdvice = advice
                )
            } catch (e: Exception) {
                _uiState.value = AdviceUiState(
                    errorMessage = e.message
                )
            }
        }
    }
}