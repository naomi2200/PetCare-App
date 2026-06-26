package com.petcare.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.petcare.app.data.repository.MedicalRepository

class MedicalViewModelFactory(
    private val repository: MedicalRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MedicalViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MedicalViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}