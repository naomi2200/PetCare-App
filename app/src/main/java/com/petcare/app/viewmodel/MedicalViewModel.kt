package com.petcare.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.petcare.app.data.local.entity.MedicalRecordEntity
import com.petcare.app.data.repository.MedicalRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MedicalViewModel(
    private val repository: MedicalRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MedicalUiState())
    val uiState: StateFlow<MedicalUiState> = _uiState.asStateFlow()

    fun loadRecords(petId: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            repository.getRecordsByPet(petId).collect { records ->
                _uiState.value = _uiState.value.copy(
                    records = records,
                    isLoading = false,
                    errorMessage = null
                )
            }
        }
    }

    fun loadRecordById(recordId: Int) {
        viewModelScope.launch {
            val record = repository.getRecordById(recordId)
            _uiState.value = _uiState.value.copy(selectedRecord = record)
        }
    }

    fun addRecord(record: MedicalRecordEntity) {
        viewModelScope.launch {
            repository.insertRecord(record)
        }
    }

    fun updateRecord(record: MedicalRecordEntity) {
        viewModelScope.launch {
            repository.updateRecord(record)
        }
    }

    fun deleteRecord(record: MedicalRecordEntity) {
        viewModelScope.launch {
            repository.deleteRecord(record)
        }
    }
}