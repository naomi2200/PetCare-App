package com.petcare.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.petcare.app.data.repository.AdviceRepository

class AdviceViewModelFactory(
    private val repository: AdviceRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AdviceViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AdviceViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}