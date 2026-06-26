package com.petcare.app.screens.pets

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.petcare.app.R
import com.petcare.app.components.PetCareButton
import com.petcare.app.components.PetCareDatePickerField
import com.petcare.app.components.PetCareImagePicker
import com.petcare.app.components.PetCareTextField
import com.petcare.app.data.local.entity.PetEntity
import com.petcare.app.ui.theme.*
import com.petcare.app.utils.FileHelper

@Composable
fun AddPetScreen(
    userId: String,
    existingPet: PetEntity? = null,
    onBackClick: () -> Unit,
    onSavePet: (PetEntity) -> Unit
) {
    val context = LocalContext.current
    var imageUri by remember { mutableStateOf(existingPet?.fotoUrl?.let { Uri.parse(it) }) }
    var name by remember { mutableStateOf(existingPet?.nombre ?: "") }
    var species by remember { mutableStateOf(existingPet?.especie ?: "") }
    var breed by remember { mutableStateOf(existingPet?.raza ?: "") }
    var age by remember { mutableStateOf(existingPet?.edad?.toString() ?: "") }
    var birthDate by remember { mutableStateOf(existingPet?.fechaNacimiento ?: "") }
    var weight by remember { mutableStateOf(existingPet?.peso?.toString() ?: "") }
    var gender by remember { mutableStateOf(existingPet?.sexo ?: "Macho") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

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
            AddPetHeader(
                title = if (existingPet != null) "Editar Mascota" else "Nueva Mascota",
                onBackClick = onBackClick
            )

            Spacer(modifier = Modifier.height(20.dp))

            // COMPONENTE REUTILIZABLE: Selector de Imagen con persistencia local
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                PetCareImagePicker(
                    imageUri = imageUri,
                    onImageSelected = { uri ->
                        uri?.let {
                            // Guardamos la imagen en el almacenamiento interno para que no desaparezca
                            val localPath = FileHelper.saveImageToInternalStorage(context, it)
                            if (localPath != null) {
                                imageUri = Uri.parse(localPath)
                            }
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                PetCareTextField(
                    value = name,
                    onValueChange = { name = it; errorMessage = null },
                    label = "Nombre de la mascota",
                    leadingIcon = Icons.Default.Person
                )

                PetCareTextField(
                    value = species,
                    onValueChange = { species = it; errorMessage = null },
                    label = "Especie (Perro, Gato, etc.)",
                    leadingIcon = Icons.Default.Pets
                )

                PetCareTextField(
                    value = breed,
                    onValueChange = { breed = it },
                    label = "Raza (Opcional)",
                    leadingIcon = Icons.Default.Pets
                )

                PetGenderSelector(selectedGender = gender, onGenderSelected = { gender = it })

                PetCareTextField(
                    value = age,
                    onValueChange = { age = it; errorMessage = null },
                    label = "Edad",
                    leadingIcon = Icons.Default.Cake
                )

                // COMPONENTE REUTILIZABLE: Selector de Fecha corregido
                Text(
                    text = "Fecha de nacimiento",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                PetCareDatePickerField(
                    value = birthDate,
                    onValueChange = { birthDate = it },
                    placeholder = "Seleccionar fecha"
                )

                PetCareTextField(
                    value = weight,
                    onValueChange = { weight = it; errorMessage = null },
                    label = "Peso actual (kg)",
                    leadingIcon = Icons.Default.Scale
                )

                errorMessage?.let {
                    Text(text = it, color = MaterialTheme.colorScheme.error, fontSize = 14.sp)
                }

                Spacer(modifier = Modifier.height(8.dp))

                PetCareButton(
                    text = if (existingPet != null) "Actualizar mascota" else "Guardar mascota",
                    onClick = {
                        val parsedAge = age.toIntOrNull()
                        val parsedWeight = weight.toDoubleOrNull()

                        when {
                            name.isBlank() -> errorMessage = "El nombre es obligatorio."
                            species.isBlank() -> errorMessage = "La especie es obligatoria."
                            parsedAge == null -> errorMessage = "Ingresa una edad válida."
                            parsedWeight == null -> errorMessage = "Ingresa un peso válido."
                            else -> {
                                onSavePet(PetEntity(
                                    id = existingPet?.id ?: 0,
                                    nombre = name.trim(),
                                    especie = species.trim(),
                                    raza = breed.ifBlank { null },
                                    sexo = gender,
                                    edad = parsedAge,
                                    peso = parsedWeight,
                                    fechaNacimiento = birthDate.ifBlank { null },
                                    fotoUrl = imageUri?.toString(),
                                    userId = userId
                                ))
                            }
                        }
                    },
                    containerColor = PrimaryPurple,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.height(100.dp))
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
fun AddPetHeader(title: String, onBackClick: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        IconButton(onClick = onBackClick, modifier = Modifier.align(Alignment.CenterStart)) {
            Icon(Icons.Default.ChevronLeft, "Volver", tint = TextPrimary, modifier = Modifier.size(32.dp))
        }
        Text(title, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
    }
}

@Composable
fun PetGenderSelector(selectedGender: String, onGenderSelected: (String) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text("Sexo", fontSize = 16.sp, color = TextSecondary, modifier = Modifier.padding(bottom = 8.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            GenderButton("Macho", selectedGender == "Macho", { onGenderSelected("Macho") }, Modifier.weight(1f))
            GenderButton("Hembra", selectedGender == "Hembra", { onGenderSelected("Hembra") }, Modifier.weight(1f))
        }
    }
}

@Composable
fun GenderButton(text: String, selected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.height(48.dp).clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        color = if (selected) PrimaryPurple else Background
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(text = text, color = if (selected) Color.White else TextSecondary, fontWeight = FontWeight.Medium)
        }
    }
}
