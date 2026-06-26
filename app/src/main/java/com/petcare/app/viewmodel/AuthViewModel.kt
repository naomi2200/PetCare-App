package com.petcare.app.viewmodel

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.petcare.app.data.auth.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repository: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun loadUserProfile() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val profile = repository.getUserProfile()
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                userProfile = profile
            )
        }
    }

    fun register(name: String, email: String, password: String, photoUrl: String? = null) {
        val error = validateRegister(name, email, password)
        if (error != null) {
            _uiState.value = _uiState.value.copy(errorMessage = error)
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                repository.register(
                    name = name.trim(),
                    email = email.trim(),
                    password = password,
                    photoUrl = photoUrl
                )
                _uiState.value = _uiState.value.copy(isSuccess = true, isLoading = false)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(errorMessage = getFriendlyError(e.message), isLoading = false)
            }
        }
    }

    fun login(email: String, password: String) {
        val error = validateLogin(email, password)
        if (error != null) {
            _uiState.value = _uiState.value.copy(errorMessage = error)
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                repository.login(
                    email = email.trim(),
                    password = password
                )
                _uiState.value = _uiState.value.copy(isSuccess = true, isLoading = false)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(errorMessage = getFriendlyError(e.message), isLoading = false)
            }
        }
    }

    fun logout() {
        repository.logout()
        _uiState.value = AuthUiState()
    }

    fun resetState() {
        _uiState.value = AuthUiState()
    }

    fun getCurrentUserId(): String? {
        return repository.getCurrentUserId()
    }

    private fun validateRegister(name: String, email: String, password: String): String? {
        return when {
            name.isBlank() -> "Ingresa tu nombre."
            email.isBlank() -> "Ingresa tu correo."
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> "Correo inválido."
            password.isBlank() -> "Ingresa tu contraseña."
            password.length < 6 -> "La contraseña debe tener al menos 6 caracteres."
            else -> null
        }
    }

    private fun validateLogin(email: String, password: String): String? {
        return when {
            email.isBlank() -> "Ingresa tu correo."
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> "Correo inválido."
            password.isBlank() -> "Ingresa tu contraseña."
            else -> null
        }
    }

    private fun getFriendlyError(message: String?): String {
        return when {
            message?.contains("email address is already in use", ignoreCase = true) == true ->
                "Este correo ya está registrado."
            message?.contains("password is invalid", ignoreCase = true) == true ->
                "Contraseña incorrecta."
            message?.contains("no user record", ignoreCase = true) == true ->
                "No existe una cuenta con este correo."
            else -> "Ocurrió un error. Inténtalo nuevamente."
        }
    }
}
