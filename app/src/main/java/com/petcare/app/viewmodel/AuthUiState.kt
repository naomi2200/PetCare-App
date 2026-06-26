package com.petcare.app.viewmodel

import com.petcare.app.data.auth.UserProfile

data class AuthUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null,
    val userProfile: UserProfile? = null
)