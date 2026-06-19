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
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.petcare.app.R
import com.petcare.app.components.PetCareButton
import com.petcare.app.components.PetCareTextField
import com.petcare.app.ui.theme.Background
import com.petcare.app.ui.theme.PrimaryPurple
import com.petcare.app.ui.theme.TextPrimary
import com.petcare.app.ui.theme.TextSecondary

@Composable
fun AddPetScreen(
    onBackClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .verticalScroll(rememberScrollState())
        ) {
            // 🎯 HEADER
            AddPetHeader(onBackClick)

            Spacer(modifier = Modifier.height(20.dp))

            // 🎯 FOTO DE LA MASCOTA
            PetPhotoSection()

            Spacer(modifier = Modifier.height(24.dp))

            // 🎯 FORMULARIO
            AddPetForm()

            Spacer(modifier = Modifier.height(40.dp))
        }

        // 🎯 MASCOTA DECORATIVA INFERIOR
        Image(
            painter = painterResource(id = R.drawable.pet_list),
            contentDescription = null,
            modifier = Modifier
                .size(120.dp)
                .align(Alignment.BottomEnd)
                .navigationBarsPadding()
                .offset(y = 10.dp, x = 10.dp),
            contentScale = ContentScale.Fit
        )
    }
}

@Composable
fun AddPetHeader(onBackClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier.align(Alignment.CenterStart)
        ) {
            Icon(
                imageVector = Icons.Default.ChevronLeft,
                contentDescription = "Volver",
                tint = TextPrimary,
                modifier = Modifier.size(32.dp)
            )
        }

        Text(
            text = "Nueva Mascota",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )
    }
}

@Composable
fun PetPhotoSection() {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        // Círculo lila de fondo
        Box(
            modifier = Modifier
                .size(130.dp)
                .clip(CircleShape)
                .background(PrimaryPurple.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            // Imagen de la mascota (Conejo en la referencia)
            Image(
                painter = painterResource(id = R.drawable.register_pet),
                contentDescription = "Mascota",
                modifier = Modifier.size(100.dp),
                contentScale = ContentScale.Fit
            )
        }

        // Botón Cámara superpuesto
        Surface(
            modifier = Modifier
                .size(40.dp)
                .align(Alignment.Center)
                .offset(x = 45.dp, y = 40.dp),
            shape = CircleShape,
            color = PrimaryPurple,
            shadowElevation = 2.dp
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Default.CameraAlt,
                    contentDescription = "Cámara",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun AddPetForm() {
    var name by remember { mutableStateOf("") }
    var species by remember { mutableStateOf("") }
    var breed by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Nombre
        PetCareTextField(
            value = name,
            onValueChange = { name = it },
            label = "Nombre",
            leadingIcon = Icons.Default.Person
        )

        // 2. Especie (UI de selector)
        PetCareTextField(
            value = species,
            onValueChange = { species = it },
            label = "Especie",
            leadingIcon = Icons.Default.Pets,
            trailingIcon = {
                Icon(Icons.Default.KeyboardArrowDown, contentDescription = null, tint = TextSecondary)
            }
        )

        // 3. Raza (UI de selector)
        PetCareTextField(
            value = breed,
            onValueChange = { breed = it },
            label = "Raza",
            leadingIcon = Icons.Default.Lock, // Reutilizando icono de candado para mantener consistencia visual si no hay uno de raza específico
            trailingIcon = {
                Icon(Icons.Default.KeyboardArrowDown, contentDescription = null, tint = TextSecondary)
            }
        )

        // 4. Sexo
        PetGenderSelector()

        // 5. Fecha de nacimiento
        PetCareTextField(
            value = birthDate,
            onValueChange = { birthDate = it },
            label = "Fecha de nacimiento",
            leadingIcon = Icons.Default.CalendarToday,
            trailingIcon = {
                Icon(Icons.Default.CalendarMonth, contentDescription = null, tint = TextSecondary)
            }
        )

        // 6. Peso
        PetCareTextField(
            value = weight,
            onValueChange = { weight = it },
            label = "Peso (kg)",
            leadingIcon = Icons.Default.Scale,
            trailingIcon = {
                Icon(Icons.Default.ChevronRight, contentDescription = null, tint = TextSecondary)
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        // BOTÓN GUARDAR
        PetCareButton(
            text = "Guardar mascota",
            onClick = { /* Solo UI */ },
            containerColor = PrimaryPurple,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun PetGenderSelector() {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Sexo",
            fontSize = 16.sp,
            color = TextSecondary,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Botón Macho (Seleccionado en la UI estática)
            Surface(
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { },
                color = PrimaryPurple,
                border = null
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(text = "Macho", color = Color.White, fontWeight = FontWeight.Medium)
                }
            }

            // Botón Hembra (No seleccionado)
            Surface(
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Background)
                    .clickable { },
                color = Background,
                border = null
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(text = "Hembra", color = TextSecondary, fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}
