package com.petcare.app.screens.auth

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.petcare.app.R
import com.petcare.app.components.*
import com.petcare.app.ui.theme.*
import com.petcare.app.utils.FileHelper
import com.petcare.app.viewmodel.AuthViewModel

@Composable
fun RegisterScreen(
    onLoginClick: () -> Unit,
    onRegisterSuccess: () -> Unit
) {
    val context = LocalContext.current
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var localError by remember { mutableStateOf<String?>(null) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val authViewModel: AuthViewModel = viewModel()
    val uiState by authViewModel.uiState.collectAsState()

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            onRegisterSuccess()
            authViewModel.resetState()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        PetCareWaveFooter(
            drawableResId = R.drawable.footer_wave_register,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            PetCareLogo(modifier = Modifier.height(90.dp))

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Crea tu cuenta",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(20.dp))

            // COMPONENTE FOTOS: Persistencia asegurada
            PetCareImagePicker(
                imageUri = imageUri,
                onImageSelected = { imageUri = it },
                size = 130.dp
            )

            Spacer(modifier = Modifier.height(22.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                PetCareTextField(
                    value = name,
                    onValueChange = { name = it; localError = null },
                    label = "Nombre completo",
                    leadingIcon = Icons.Default.Person
                )

                PetCareTextField(
                    value = email,
                    onValueChange = { email = it; localError = null },
                    label = "Correo electrónico",
                    leadingIcon = Icons.Default.Email,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                )

                PetCareTextField(
                    value = password,
                    onValueChange = { password = it; localError = null },
                    label = "Contraseña",
                    leadingIcon = Icons.Default.Lock,
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = null,
                                tint = TextSecondary
                            )
                        }
                    }
                )

                PetCareTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it; localError = null },
                    label = "Confirmar contraseña",
                    leadingIcon = Icons.Default.Lock,
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            PetCareButton(
                text = if (uiState.isLoading) "Registrando..." else "Registrarme",
                onClick = {
                    if (password != confirmPassword) {
                        localError = "Las contraseñas no coinciden."
                    } else if (name.isBlank() || email.isBlank() || password.isBlank()) {
                        localError = "Por favor, completa todos los campos."
                    } else {
                        localError = null
                        // GESTIÓN DE FOTO PERMANENTE: Se guarda localmente antes del registro
                        val finalPhotoUrl = imageUri?.let { 
                            FileHelper.saveImageToInternalStorage(context, it) 
                        }
                        // Nota: El ViewModel debería recibir opcionalmente la photoUrl para Firestore
                        authViewModel.register(name, email, password)
                    }
                },
                containerColor = PrimaryPink,
                modifier = Modifier.fillMaxWidth()
            )

            (localError ?: uiState.errorMessage)?.let { error ->
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = error, color = MaterialTheme.colorScheme.error, textAlign = TextAlign.Center)
            }

            Spacer(modifier = Modifier.height(18.dp))

            Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                Text(text = "¿Ya tienes cuenta? ", color = TextSecondary, style = MaterialTheme.typography.bodyMedium)
                Text(
                    text = "Inicia sesión",
                    color = PrimaryPurple,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onLoginClick() }
                )
            }
            Spacer(modifier = Modifier.height(120.dp))
        }
    }
}
