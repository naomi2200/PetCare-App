package com.petcare.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.petcare.app.data.local.entity.PetEntity
import com.petcare.app.data.repository.PetRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PetViewModel(
    private val repository: PetRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PetUiState())
    val uiState: StateFlow<PetUiState> = _uiState.asStateFlow()

    fun loadPets(userId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            repository.getPetsByUser(userId).collect { pets ->
                _uiState.value = _uiState.value.copy(
                    pets = pets,
                    isLoading = false,
                    errorMessage = null
                )
            }
        }
    }

    fun loadPetById(petId: Int) {
        viewModelScope.launch {
            val pet = repository.getPetById(petId)
            _uiState.value = _uiState.value.copy(selectedPet = pet)
        }
    }

    fun addPet(pet: PetEntity) {
        viewModelScope.launch {
            repository.insertPet(pet)
        }
    }

    fun updatePet(pet: PetEntity) {
        viewModelScope.launch {
            repository.updatePet(pet)
        }
    }

    fun deletePet(pet: PetEntity) {
        viewModelScope.launch {
            repository.deletePet(pet)
        }
    }
}