package com.petcare.app.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.petcare.app.R
import com.petcare.app.components.PetCareButton
import com.petcare.app.components.PetCareLogo
import com.petcare.app.components.PetCareTextField
import com.petcare.app.components.PetCareWaveFooter
import com.petcare.app.ui.theme.Background
import com.petcare.app.ui.theme.PrimaryPink
import com.petcare.app.ui.theme.PrimaryPurple
import com.petcare.app.ui.theme.TextPrimary
import com.petcare.app.ui.theme.TextSecondary

@Composable
fun RegisterScreen(
    onLoginClick: () -> Unit,
    onRegisterSuccess: () -> Unit
) {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        // 🌊 Colocado al inicio del Box para que sirva de fondo limpio sin empujar componentes
        PetCareWaveFooter(
            drawableResId = R.drawable.footer_wave_register,
            modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 28.dp), // Ajustado para coincidir simétricamente con Login
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 🎯 Espacio controlado para el notch
            Spacer(modifier = Modifier.height(24.dp))

            // 🎯 1. LOGO: Reducido un 35% y con altura fija para control estricto
            PetCareLogo(
                modifier = Modifier.height(52.dp)
            )

            // 🎯 Reducción de espacio vertical entre Logo y Título
            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Crea tu cuenta",
                style = MaterialTheme.typography.headlineMedium,
                color = TextPrimary
            )

            // 🎯 Reducción de espacio vertical entre Título y Avatar
            Spacer(modifier = Modifier.height(14.dp))

            // 🎯 2. AVATAR: Incrementado un 20% para ser el punto focal definitivo de la pantalla
            Box(
                modifier = Modifier.size(150.dp), // Escalado a 150.dp
                contentAlignment = Alignment.BottomEnd
            ) {
                Image(
                    painter = painterResource(id = R.drawable.pet_register),
                    contentDescription = "Avatar Puppy Register",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .background(PrimaryPurple.copy(alpha = 0.15f))
                        .border(2.dp, Color.White, CircleShape)
                )
                IconButton(
                    onClick = { /* No-op */ },
                    colors = IconButtonDefaults.iconButtonColors(containerColor = PrimaryPurple),
                    modifier = Modifier
                        .size(38.dp) // Proporcional al nuevo tamaño del avatar
                        .offset(x = 4.dp, y = 4.dp)
                        .clip(CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = "Change avatar",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            // 🎯 Reducción de espacio vertical entre Avatar y primer TextField
            Spacer(modifier = Modifier.height(16.dp))

            // 🎯 3. FORMULARIO COMPACTO
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp) // Espaciado corto y uniforme
            ) {
                PetCareTextField(
                    value = fullName,
                    onValueChange = { fullName = it },
                    label = "Nombre completo",
                    leadingIcon = Icons.Default.Person
                )

                PetCareTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = "Correo electrónico",
                    leadingIcon = Icons.Default.Email,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                )

                PetCareTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = "Contraseña",
                    leadingIcon = Icons.Default.Lock,
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    trailingIcon = {
                        val image = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(imageVector = image, contentDescription = null, tint = TextSecondary)
                        }
                    }
                )

                PetCareTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = "Confirmar contraseña",
                    leadingIcon = Icons.Default.Lock,
                    visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    trailingIcon = {
                        val image = if (confirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                        IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                            Icon(imageVector = image, contentDescription = null, tint = TextSecondary)
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 🎯 4. BOTÓN PRINCIPAL
            PetCareButton(
                text = "Registrarme",
                onClick = onRegisterSuccess,
                containerColor = PrimaryPink,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 🎯 5. TEXTO DE NAVEGACIÓN
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "¿Ya tienes cuenta? ", color = TextSecondary, style = MaterialTheme.typography.bodyMedium)
                Text(
                    text = "Inicia sesión",
                    color = PrimaryPurple,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onLoginClick() }
                )
            }

            // 🎯 Ajustado a un margen de seguridad cómodo de 60.dp para no cortar la ola inferior
            Spacer(modifier = Modifier.height(60.dp))
        }
    }
}