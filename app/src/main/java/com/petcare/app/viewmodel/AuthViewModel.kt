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

    fun register(name: String, email: String, password: String) {
        val error = validateRegister(name, email, password)
        if (error != null) {
            _uiState.value = AuthUiState(errorMessage = error)
            return
        }

        viewModelScope.launch {
            _uiState.value = AuthUiState(isLoading = true)
            try {
                repository.register(
                    name = name.trim(),
                    email = email.trim(),
                    password = password
                )
                _uiState.value = AuthUiState(isSuccess = true)
            } catch (e: Exception) {
                _uiState.value = AuthUiState(errorMessage = getFriendlyError(e.message))
            }
        }
    }

    fun login(email: String, password: String) {
        val error = validateLogin(email, password)
        if (error != null) {
            _uiState.value = AuthUiState(errorMessage = error)
            return
        }

        viewModelScope.launch {
            _uiState.value = AuthUiState(isLoading = true)
            try {
                repository.login(
                    email = email.trim(),
                    password = password
                )
                _uiState.value = AuthUiState(isSuccess = true)
            } catch (e: Exception) {
                _uiState.value = AuthUiState(errorMessage = getFriendlyError(e.message))
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