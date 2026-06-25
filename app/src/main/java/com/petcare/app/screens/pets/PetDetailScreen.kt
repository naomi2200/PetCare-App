package com.petcare.app.screens.pets

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.petcare.app.R
import com.petcare.app.data.local.entity.PetEntity
import com.petcare.app.ui.theme.*

@Composable
fun PetDetailScreen(
    pet: PetEntity?,
    onBackClick: () -> Unit,
    onEditClick: () -> Unit = {},
    onMedicalHistoryClick: () -> Unit = {},
    onWeightTrackingClick: () -> Unit = {},
    onRemindersClick: () -> Unit = {},
    onDeleteClick: (PetEntity) -> Unit
) {
    Scaffold(
        bottomBar = { BottomNavigationBar() },
        containerColor = Background
    ) { paddingValues ->
        if (pet == null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("No se encontró la mascota.", color = TextPrimary)
                Spacer(modifier = Modifier.height(12.dp))
                Button(onClick = onBackClick) {
                    Text("Volver")
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
            ) {
                PetHeader(onBackClick, onEditClick)

                PetInfoCard(
                    pet = pet,
                    modifier = Modifier
                        .padding(horizontal = 24.dp)
                        .offset(y = (-40).dp)
                )

                PetStatsRow(
                    pet = pet,
                    modifier = Modifier
                        .padding(horizontal = 24.dp)
                        .offset(y = (-20).dp)
                )

                PetMenuSection(
                    onMedicalHistoryClick = onMedicalHistoryClick,
                    onWeightTrackingClick = onWeightTrackingClick,
                    onRemindersClick = onRemindersClick,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedButton(
                    onClick = { onDeleteClick(pet) },
                    modifier = Modifier
                        .padding(horizontal = 24.dp)
                        .fillMaxWidth()
                ) {
                    Text("Eliminar mascota")
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun PetHeader(onBackClick: () -> Unit, onEditClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp)
            .clip(RoundedCornerShape(bottomStart = 40.dp, bottomEnd = 40.dp))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        PrimaryPurple.copy(alpha = 0.4f),
                        PrimaryPurple.copy(alpha = 0.05f)
                    )
                )
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 20.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .background(Color.White, CircleShape)
                    .size(40.dp)
            ) {
                Icon(Icons.Default.ChevronLeft, contentDescription = null, tint = PrimaryPurple)
            }

            IconButton(
                onClick = onEditClick,
                modifier = Modifier
                    .background(Color.White, CircleShape)
                    .size(40.dp)
            ) {
                Icon(Icons.Default.Edit, contentDescription = null, tint = PrimaryPurple)
            }
        }

        Image(
            painter = painterResource(id = R.drawable.pet_login),
            contentDescription = null,
            modifier = Modifier
                .size(200.dp)
                .align(Alignment.Center),
            contentScale = ContentScale.Fit
        )
    }
}

@Composable
fun PetInfoCard(
    pet: PetEntity,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        color = Color.White,
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.pet_login),
                contentDescription = null,
                modifier = Modifier
                    .size(65.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = pet.nombre,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    Icon(
                        imageVector = if (pet.sexo == "Macho") Icons.Default.Male else Icons.Default.Female,
                        contentDescription = null,
                        tint = if (pet.sexo == "Macho") Color(0xFF5D7BFF) else PrimaryPink,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Text(
                    text = pet.raza ?: pet.especie,
                    fontSize = 15.sp,
                    color = TextSecondary
                )
            }
        }
    }
}

@Composable
fun PetStatsRow(
    pet: PetEntity,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        StatItem("Edad", "${pet.edad} años", Modifier.weight(1f))
        StatItem("Peso", "${pet.peso} kg", Modifier.weight(1f))
        StatItem("Nacimiento", pet.fechaNacimiento ?: "Sin dato", Modifier.weight(1f))
    }
}

@Composable
fun StatItem(label: String, value: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .background(Color(0xFFF2F3F7), RoundedCornerShape(20.dp))
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(value, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        Text(label, fontSize = 13.sp, color = TextSecondary)
    }
}

@Composable
fun PetMenuSection(
    onMedicalHistoryClick: () -> Unit,
    onWeightTrackingClick: () -> Unit,
    onRemindersClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        PetMenuItem(
            icon = Icons.AutoMirrored.Filled.Assignment,
            title = "Historial médico",
            bgColor = PrimaryPink.copy(alpha = 0.15f),
            tint = PrimaryPink,
            onClick = onMedicalHistoryClick
        )

        PetMenuItem(
            icon = Icons.Default.MonitorWeight,
            title = "Seguimiento de peso",
            bgColor = PrimaryPurple.copy(alpha = 0.15f),
            tint = PrimaryPurple,
            onClick = onWeightTrackingClick
        )

        PetMenuItem(
            icon = Icons.Default.NotificationsActive,
            title = "Recordatorios",
            bgColor = WarningYellow.copy(alpha = 0.4f),
            tint = Color(0xFFF59E0B),
            onClick = onRemindersClick
        )
    }
}

@Composable
fun PetMenuItem(
    icon: ImageVector,
    title: String,
    bgColor: Color,
    tint: Color,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(18.dp),
        color = Color.White
    ) {
        Row(
            modifier = Modifier
                .padding(14.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(bgColor, RoundedCornerShape(14.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, null, tint = tint, modifier = Modifier.size(26.dp))
            }

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                title,
                fontSize = 17.sp,
                fontWeight = FontWeight.Medium,
                color = TextPrimary,
                modifier = Modifier.weight(1f)
            )

            Icon(Icons.Default.ChevronRight, null, tint = Color.LightGray, modifier = Modifier.size(24.dp))
        }
    }
}