package com.petcare.app.screens.profile

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.HelpOutline
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.petcare.app.components.PetCareButton
import com.petcare.app.components.PetCareImagePicker
import com.petcare.app.screens.pets.BottomNavigationBar
import com.petcare.app.ui.theme.*

@Composable
fun ProfileScreen(
    userPhotoUrl: String? = null,
    onPhotoSelected: (Uri?) -> Unit = {}, // Nuevo callback para cuando el usuario elija una foto
    onOptionClick: (String) -> Unit = {},
    onLogoutClick: () -> Unit = {}
) {
    // Estado local para manejar la previsualización de la foto seleccionada
    var currentPhotoUri by remember {
        mutableStateOf<Uri?>(userPhotoUrl?.let { Uri.parse(it) })
    }

    Scaffold(
        bottomBar = {
            BottomNavigationBar()
        },
        containerColor = Background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // --- CABECERA ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(PrimaryPurple, PrimaryPurple.copy(alpha = 0.8f))
                            )
                        )
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 40.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    // --- CAMBIO AQUÍ: Usamos PetCareImagePicker ---
                    PetCareImagePicker(
                        imageUri = currentPhotoUri,
                        onImageSelected = { uri ->
                            currentPhotoUri = uri
                            onPhotoSelected(uri) // Avisamos al exterior que cambió la foto
                        },
                        size = 85.dp // Mantenemos el tamaño del diseño original
                    )

                    Spacer(modifier = Modifier.width(20.dp))

                    Column {
                        Text(
                            text = "Adriana",
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "adriano@email.com",
                            fontSize = 15.sp,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }
                }

                // --- TARJETA ESTADÍSTICAS ---
                Card(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(horizontal = 24.dp)
                        .fillMaxWidth()
                        .height(90.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        StatItem(modifier = Modifier.weight(1f), count = "3", label = "Mascotas")
                        VerticalDivider(modifier = Modifier.height(40.dp), color = Border.copy(alpha = 0.5f))
                        StatItem(modifier = Modifier.weight(1f), count = "12", label = "Recordatorios")
                        VerticalDivider(modifier = Modifier.height(40.dp), color = Border.copy(alpha = 0.5f))
                        StatItem(modifier = Modifier.weight(1f), count = "8", label = "Consultas")
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // --- LISTA DE OPCIONES ---
            Card(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(vertical = 8.dp)) {
                    ProfileOptionItem(Icons.Outlined.Person, "Información personal") { onOptionClick("info") }
                    HorizontalDivider(Modifier.padding(horizontal = 20.dp), color = Border.copy(alpha = 0.3f))

                    ProfileOptionItem(Icons.Outlined.Notifications, "Notificaciones") { onOptionClick("notif") }
                    HorizontalDivider(Modifier.padding(horizontal = 20.dp), color = Border.copy(alpha = 0.3f))

                    ProfileOptionItem(Icons.AutoMirrored.Outlined.HelpOutline, "Ayuda y soporte") { onOptionClick("help") }
                    HorizontalDivider(Modifier.padding(horizontal = 20.dp), color = Border.copy(alpha = 0.3f))

                    ProfileOptionItem(Icons.Outlined.Info, "Acerca de PetCare") { onOptionClick("about") }
                }
            }

            Spacer(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.height(32.dp))

            // --- BOTÓN CERRAR SESIÓN ---
            Box(modifier = Modifier.padding(horizontal = 24.dp, vertical = 24.dp)) {
                PetCareButton(
                    text = "Cerrar sesión",
                    onClick = onLogoutClick,
                    containerColor = PrimaryPurple
                )
            }
        }
    }
}

@Composable
private fun StatItem(modifier: Modifier, count: String, label: String) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(count, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        Text(label, fontSize = 13.sp, color = TextSecondary)
    }
}

@Composable
private fun ProfileOptionItem(icon: ImageVector, title: String, onClick: () -> Unit) {
    Surface(onClick = onClick, color = Color.Transparent) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, null, tint = TextPrimary, modifier = Modifier.size(24.dp))
            Spacer(Modifier.width(16.dp))
            Text(title, Modifier.weight(1f), fontSize = 16.sp, fontWeight = FontWeight.Medium, color = TextPrimary)
            Icon(Icons.AutoMirrored.Outlined.KeyboardArrowRight, null, tint = TextSecondary, modifier = Modifier.size(20.dp))
        }
    }
}