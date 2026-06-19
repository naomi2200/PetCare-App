package com.petcare.app.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
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
        // 🌊 FOOTER: Visible y anclado al final, respetando la barra de navegación
        PetCareWaveFooter(
            drawableResId = R.drawable.footer_wave_login,
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
            Spacer(modifier = Modifier.height(30.dp))

            // 🎯 LOGO (80dp - Sin cambios)
            PetCareLogo(
                modifier = Modifier.height(80.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 🎯 TÍTULO (Reducido a 28sp para ajuste de jerarquía)
            Text(
                text = "¡Bienvenido de vuelta!",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                textAlign = TextAlign.Center
            )

            // 🎯 SUBTÍTULO
            Text(
                text = "Inicia sesión para continuar",
                fontSize = 16.sp,
                color = TextSecondary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 🎯 MASCOTA Y HUELLITAS DECORATIVAS
            Box(
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                // Mascota principal (180dp - Sin cambios)
                Image(
                    painter = painterResource(id = R.drawable.pet_login),
                    contentDescription = "PetCare Login Mascot",
                    modifier = Modifier.size(180.dp)
                )

                // Huella superior (Opacidad aumentada a 0.40f)
                Image(
                    painter = painterResource(id = R.drawable.paw_background),
                    contentDescription = null,
                    modifier = Modifier
                        .offset(x = 115.dp, y = (-50).dp)
                        .size(36.dp)
                        .alpha(0.50f)
                )

                // Huella inferior (Opacidad aumentada a 0.35f)
                Image(
                    painter = painterResource(id = R.drawable.paw_background),
                    contentDescription = null,
                    modifier = Modifier
                        .offset(x = 145.dp, y = 15.dp)
                        .size(28.dp)
                        .alpha(0.50f)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 🎯 CAMPOS FORMULARIO
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
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

            // 🎯 ENLACE RECUPERAR CONTRASEÑA
            Text(
                text = "¿Olvidaste tu contraseña?",
                color = PrimaryPurple,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.clickable { /* No-op */ }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 🎯 BOTÓN INICIAR SESIÓN
            PetCareButton(
                text = "Iniciar sesión",
                onClick = onLoginSuccess,
                containerColor = PrimaryPurple,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 🎯 TEXTO REGISTRARSE
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "¿No tienes cuenta? ",
                    color = TextSecondary,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Regístrate",
                    color = PrimaryPurple,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onRegisterClick() }
                )
            }

            // 🎯 ESPACIADO FINAL AUMENTADO (160dp)
            // Esto empuja el texto hacia arriba, creando el espacio libre solicitado
            // antes de que comience la ola del footer.
            Spacer(modifier = Modifier.height(160.dp))
        }
    }
}