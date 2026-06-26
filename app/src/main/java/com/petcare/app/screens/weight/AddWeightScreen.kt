package com.petcare.app.screens.weight

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.petcare.app.components.PetCareButton
import com.petcare.app.components.PetCareDatePickerField
import com.petcare.app.components.PetCareTextField
import com.petcare.app.data.local.entity.PetEntity
import com.petcare.app.data.local.entity.WeightEntryEntity
import com.petcare.app.ui.theme.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddWeightScreen(
    pets: List<PetEntity>,
    initialPetId: Int,
    onBack: () -> Unit,
    onSave: (WeightEntryEntity) -> Unit
) {
    var selectedPet by remember { mutableStateOf(pets.find { it.id == initialPetId }) }
    var weight by remember { mutableStateOf("") }
    
    // Estandarización UTC para la fecha inicial
    val dateValue = remember {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }
        sdf.format(Date())
    }
    var date by remember { mutableStateOf(dateValue) }
    var expanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Registrar peso", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = TextPrimary) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = null, tint = TextPrimary)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Background)
            )
        },
        containerColor = Background
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(PrimaryPurple.copy(alpha = 0.1f), RoundedCornerShape(24.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Outlined.Scale, null, tint = PrimaryPurple, modifier = Modifier.size(48.dp))
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Selector de Mascota
            Column(modifier = Modifier.fillMaxWidth()) {
                Text("Mascota", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextPrimary, modifier = Modifier.padding(bottom = 8.dp))
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = selectedPet?.nombre ?: "Seleccionar mascota",
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable).fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryPurple,
                            unfocusedBorderColor = Border
                        )
                    )
                    ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        pets.forEach { pet ->
                            DropdownMenuItem(
                                text = { Text(pet.nombre) },
                                onClick = {
                                    selectedPet = pet
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Peso
            PetCareTextField(
                value = weight,
                onValueChange = { weight = it },
                label = "Peso actual (kg)",
                leadingIcon = Icons.Outlined.MonitorWeight
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Fecha del registro
            Column(modifier = Modifier.fillMaxWidth()) {
                Text("Fecha del registro", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextPrimary, modifier = Modifier.padding(bottom = 8.dp))
                PetCareDatePickerField(
                    value = date,
                    onValueChange = { date = it },
                    placeholder = "Seleccionar fecha"
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            PetCareButton(
                text = "Guardar registro",
                onClick = {
                    val w = weight.toDoubleOrNull()
                    val p = selectedPet
                    if (w != null && p != null) {
                        onSave(WeightEntryEntity(
                            petId = p.id,
                            weight = w,
                            date = date
                        ))
                    }
                },
                containerColor = PrimaryPurple,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}