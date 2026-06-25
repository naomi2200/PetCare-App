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
import com.petcare.app.data.local.entity.PetEntity
import com.petcare.app.ui.theme.*

@Composable
fun AddPetScreen(
    userId: String,
    onBackClick: () -> Unit,
    onSavePet: (PetEntity) -> Unit
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
            AddPetHeader(onBackClick)

            Spacer(modifier = Modifier.height(20.dp))

            PetPhotoSection()

            Spacer(modifier = Modifier.height(24.dp))

            AddPetForm(
                userId = userId,
                onSavePet = onSavePet
            )

            Spacer(modifier = Modifier.height(40.dp))
        }

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
        Box(
            modifier = Modifier
                .size(130.dp)
                .clip(CircleShape)
                .background(PrimaryPurple.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.register_pet),
                contentDescription = "Mascota",
                modifier = Modifier.size(100.dp),
                contentScale = ContentScale.Fit
            )
        }

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
fun AddPetForm(
    userId: String,
    onSavePet: (PetEntity) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var species by remember { mutableStateOf("") }
    var breed by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("Macho") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        PetCareTextField(
            value = name,
            onValueChange = {
                name = it
                errorMessage = null
            },
            label = "Nombre",
            leadingIcon = Icons.Default.Person
        )

        PetCareTextField(
            value = species,
            onValueChange = {
                species = it
                errorMessage = null
            },
            label = "Especie",
            leadingIcon = Icons.Default.Pets,
            trailingIcon = {
                Icon(Icons.Default.KeyboardArrowDown, contentDescription = null, tint = TextSecondary)
            }
        )

        PetCareTextField(
            value = breed,
            onValueChange = { breed = it },
            label = "Raza",
            leadingIcon = Icons.Default.Pets,
            trailingIcon = {
                Icon(Icons.Default.KeyboardArrowDown, contentDescription = null, tint = TextSecondary)
            }
        )

        PetGenderSelector(
            selectedGender = gender,
            onGenderSelected = { gender = it }
        )

        PetCareTextField(
            value = age,
            onValueChange = {
                age = it
                errorMessage = null
            },
            label = "Edad",
            leadingIcon = Icons.Default.Cake
        )

        PetCareTextField(
            value = birthDate,
            onValueChange = { birthDate = it },
            label = "Fecha de nacimiento",
            leadingIcon = Icons.Default.CalendarToday,
            trailingIcon = {
                Icon(Icons.Default.CalendarMonth, contentDescription = null, tint = TextSecondary)
            }
        )

        PetCareTextField(
            value = weight,
            onValueChange = {
                weight = it
                errorMessage = null
            },
            label = "Peso (kg)",
            leadingIcon = Icons.Default.Scale,
            trailingIcon = {
                Icon(Icons.Default.ChevronRight, contentDescription = null, tint = TextSecondary)
            }
        )

        errorMessage?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        PetCareButton(
            text = "Guardar mascota",
            onClick = {
                val parsedAge = age.toIntOrNull()
                val parsedWeight = weight.toDoubleOrNull()

                when {
                    name.isBlank() -> errorMessage = "Ingresa el nombre de la mascota."
                    species.isBlank() -> errorMessage = "Ingresa la especie."
                    parsedAge == null -> errorMessage = "Ingresa una edad válida."
                    parsedWeight == null -> errorMessage = "Ingresa un peso válido."
                    else -> {
                        val pet = PetEntity(
                            nombre = name.trim(),
                            especie = species.trim(),
                            raza = breed.ifBlank { null },
                            sexo = gender,
                            edad = parsedAge,
                            peso = parsedWeight,
                            fechaNacimiento = birthDate.ifBlank { null },
                            fotoUrl = null,
                            userId = userId
                        )
                        onSavePet(pet)
                    }
                }
            },
            containerColor = PrimaryPurple,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun PetGenderSelector(
    selectedGender: String,
    onGenderSelected: (String) -> Unit
) {
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
            GenderButton(
                text = "Macho",
                selected = selectedGender == "Macho",
                onClick = { onGenderSelected("Macho") },
                modifier = Modifier.weight(1f)
            )

            GenderButton(
                text = "Hembra",
                selected = selectedGender == "Hembra",
                onClick = { onGenderSelected("Hembra") },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun GenderButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .height(48.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() },
        color = if (selected) PrimaryPurple else Background
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = text,
                color = if (selected) Color.White else TextSecondary,
                fontWeight = FontWeight.Medium
            )
        }
    }
}