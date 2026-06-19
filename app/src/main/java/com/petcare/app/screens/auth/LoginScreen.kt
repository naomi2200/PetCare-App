package com.petcare.app.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.petcare.app.R
import com.petcare.app.components.PetCareButton
import com.petcare.app.components.PetCareLogo
import com.petcare.app.components.PetCareTextField
import com.petcare.app.components.PetCareWaveFooter
import com.petcare.app.ui.theme.Background
import com.petcare.app.ui.theme.PrimaryPurple
import com.petcare.app.ui.theme.TextPrimary
import com.petcare.app.ui.theme.TextSecondary

@Composable
fun LoginScreen(
    onRegisterClick: () -> Unit,
    onLoginSuccess: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        // 🌊 Fondo decorativo anclado abajo sin interferir en el flujo de la columna
        PetCareWaveFooter(
            drawableResId = R.drawable.footer_wave_login,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 28.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 🎯 Aire inicial balanceado para el Notch/Status Bar
            Spacer(modifier = Modifier.height(36.dp))

            // 🎯 1. LOGO: Mediano y con la presencia solicitada (Rango 65dp - 75dp)
            PetCareLogo(
                modifier = Modifier.height(70.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 🎯 2. TEXTOS DE BIENVENIDA (Espaciado natural, no aplastado)
            Text(
                text = "¡Bienvenido de vuelta!",
                style = MaterialTheme.typography.headlineMedium,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Inicia sesión para continuar",
                style = MaterialTheme.typography.titleMedium,
                color = TextSecondary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 🎯 3. MASCOTA: Recupera su protagonismo visual absoluto (Fijado en 170dp)
            Image(
                painter = painterResource(id = R.drawable.pet_login),
                contentDescription = "PetCare Login Mascot",
                modifier = Modifier.size(170.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 🎯 4. FORMULARIO CÓMODO
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
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
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 🎯 5. RECUPERACIÓN DE CONTRASEÑA
            Text(
                text = "¿Olvidaste tu contraseña?",
                color = PrimaryPurple,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.clickable { /* No-op */ }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 🎯 6. BOTÓN PRINCIPAL
            PetCareButton(
                text = "Iniciar sesión",
                onClick = onLoginSuccess,
                containerColor = PrimaryPurple,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(18.dp))

            // 🎯 7. TEXTO REGÍSTRATE
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "¿No tienes cuenta? ", color = TextSecondary, style = MaterialTheme.typography.bodyMedium)
                Text(
                    text = "Regístrate",
                    color = PrimaryPurple,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onRegisterClick() }
                )
            }

            // 🎯 Colchón de seguridad para que la ola respire en cualquier pantalla
            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}