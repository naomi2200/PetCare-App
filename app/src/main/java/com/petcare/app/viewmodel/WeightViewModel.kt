package com.petcare.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.petcare.app.data.local.entity.WeightEntryEntity
import com.petcare.app.data.repository.WeightRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WeightViewModel(
    private val repository: WeightRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(WeightUiState())
    val uiState: StateFlow<WeightUiState> = _uiState.asStateFlow()

    fun loadWeights(petId: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            repository.getWeightsByPet(petId).collect { weights ->
                _uiState.value = _uiState.value.copy(
                    weights = weights,
                    isLoading = false,
                    errorMessage = null
                )
            }
        }
    }

    fun loadLastWeight(petId: Int) {
        viewModelScope.launch {
            repository.getLastWeightByPet(petId).collect { lastWeight ->
                _uiState.value = _uiState.value.copy(lastWeight = lastWeight)
            }
        }
    }

    fun loadWeightById(weightId: Int) {
        viewModelScope.launch {
            val weight = repository.getWeightById(weightId)
            _uiState.value = _uiState.value.copy(selectedWeight = weight)
        }
    }

    fun addWeight(weight: WeightEntryEntity) {
        viewModelScope.launch {
            repository.insertWeight(weight)
        }
    }

    fun updateWeight(weight: WeightEntryEntity) {
        viewModelScope.launch {
            repository.updateWeight(weight)
        }
    }

    fun deleteWeight(weight: WeightEntryEntity) {
        viewModelScope.launch {
            repository.deleteWeight(weight)
        }
    }
}
