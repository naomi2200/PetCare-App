package com.petcare.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.petcare.app.data.local.entity.ReminderEntity
import com.petcare.app.data.repository.ReminderRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ReminderViewModel(
    private val repository: ReminderRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReminderUiState())
    val uiState: StateFlow<ReminderUiState> = _uiState.asStateFlow()

    private var remindersJob: Job? = null

    fun loadRemindersByPet(petId: Int) {
        remindersJob?.cancel()
        remindersJob = viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            repository.getRemindersByPet(petId).collect { reminders ->
                _uiState.update { it.copy(
                    reminders = reminders,
                    isLoading = false,
                    errorMessage = null
                ) }
            }
        }
    }

    fun loadAllReminders() {
        remindersJob?.cancel()
        remindersJob = viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            repository.getAllReminders().collect { reminders ->
                _uiState.update { it.copy(
                    reminders = reminders,
                    isLoading = false,
                    errorMessage = null
                ) }
            }
        }
    }

    fun loadReminderById(reminderId: Int) {
        viewModelScope.launch {
            val reminder = repository.getReminderById(reminderId)
            _uiState.update { it.copy(selectedReminder = reminder) }
        }
    }

    suspend fun addReminder(reminder: ReminderEntity): Long {
        return repository.insertReminder(reminder)
    }

    fun updateReminder(reminder: ReminderEntity) {
        viewModelScope.launch {
            repository.updateReminder(reminder)
        }
    }

    fun deleteReminder(reminder: ReminderEntity) {
        viewModelScope.launch {
            repository.deleteReminder(reminder)
        }
    }

    fun markAsCompleted(reminder: ReminderEntity) {
        viewModelScope.launch {
            repository.updateReminder(
                reminder.copy(isCompleted = true)
            )
        }
    }
}
