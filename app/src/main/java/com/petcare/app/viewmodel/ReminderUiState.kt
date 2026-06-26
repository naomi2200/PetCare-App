package com.petcare.app.viewmodel

import com.petcare.app.data.local.entity.ReminderEntity

data class ReminderUiState(
    val reminders: List<ReminderEntity> = emptyList(),
    val selectedReminder: ReminderEntity? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)