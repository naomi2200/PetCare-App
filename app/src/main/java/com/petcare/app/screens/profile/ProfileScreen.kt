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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.petcare.app.components.PetCareButton
import com.petcare.app.components.PetCareImagePicker
import com.petcare.app.screens.pets.BottomNavigationBar
import com.petcare.app.ui.theme.*
import com.petcare.app.utils.FileHelper

@Composable
fun ProfileScreen(
    userName: String = "Usuario",
    userEmail: String = "",
    petCount: Int = 0,
    reminderCount: Int = 0,
    medicalCount: Int = 0,
    userPhotoUrl: String? = null,
    onPhotoSelected: (String) -> Unit = {},
    onLogoutClick: () -> Unit = {},
    onHomeClick: () -> Unit = {},
    onPetsClick: () -> Unit = {},
    onRemindersClick: () -> Unit = {}
) {
    val context = LocalContext.current
    var currentPhotoUri by remember {
        mutableStateOf(userPhotoUrl?.let { Uri.parse(it) })
    }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                onHomeClick = onHomeClick,
                onPetsClick = onPetsClick,
                onRemindersClick = onRemindersClick,
                onProfileClick = { /* Ya estamos aquí */ }
            )
        },
        containerColor = Background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
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
                    // COMPONENTE FOTOS: Persistencia local
                    PetCareImagePicker(
                        imageUri = currentPhotoUri,
                        onImageSelected = { uri ->
                            uri?.let {
                                val localPath = FileHelper.saveImageToInternalStorage(context, it)
                                if (localPath != null) {
                                    currentPhotoUri = Uri.parse(localPath)
                                    onPhotoSelected(localPath)
                                }
                            }
                        },
                        size = 85.dp
                    )

                    Spacer(modifier = Modifier.width(20.dp))

                    Column {
                        Text(
                            text = userName,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = userEmail,
                            fontSize = 14.sp,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }
                }

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
                        StatItem(modifier = Modifier.weight(1f), count = petCount.toString(), label = "Mascotas")
                        VerticalDivider(modifier = Modifier.height(40.dp), color = Border.copy(alpha = 0.5f))
                        StatItem(modifier = Modifier.weight(1f), count = reminderCount.toString(), label = "Recordatorios")
                        VerticalDivider(modifier = Modifier.height(40.dp), color = Border.copy(alpha = 0.5f))
                        StatItem(modifier = Modifier.weight(1f), count = medicalCount.toString(), label = "Consultas")
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Card(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(vertical = 8.dp)) {
                    ProfileOptionItem(Icons.Outlined.Person, "Información personal") { }
                    HorizontalDivider(Modifier.padding(horizontal = 20.dp), color = Border.copy(alpha = 0.3f))

                    ProfileOptionItem(Icons.Outlined.Notifications, "Notificaciones") { }
                    HorizontalDivider(Modifier.padding(horizontal = 20.dp), color = Border.copy(alpha = 0.3f))

                    ProfileOptionItem(Icons.AutoMirrored.Outlined.HelpOutline, "Ayuda y soporte") { }
                    HorizontalDivider(Modifier.padding(horizontal = 20.dp), color = Border.copy(alpha = 0.3f))

                    ProfileOptionItem(Icons.Outlined.Info, "Acerca de PetCare") { }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Box(modifier = Modifier.padding(horizontal = 24.dp, vertical = 24.dp)) {
                PetCareButton(
                    text = "Cerrar sesión",
                    onClick = onLogoutClick,
                    containerColor = PrimaryPurple,
                    modifier = Modifier.fillMaxWidth()
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