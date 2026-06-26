package com.petcare.app.screens.reminders

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.petcare.app.components.PetCareButton
import com.petcare.app.components.PetCareDatePickerField
import com.petcare.app.components.PetCareTimePickerField
import com.petcare.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReminderFormScreen(
    isEdit: Boolean = false,
    onBackClick: () -> Unit = {}
) {
    var title by remember { mutableStateOf("") }
    var selectedPet by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }
    var selectedRepeat by remember { mutableStateOf("No se repite") }
    var observations by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = if (isEdit) "Editar recordatorio" else "Agregar recordatorio",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = TextPrimary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = "Atrás",
                            tint = TextPrimary
                        )
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
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Programa un recordatorio para el cuidado de tu mascota.",
                fontSize = 15.sp,
                color = TextSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // Título
            ReminderFormField(
                label = "Título del recordatorio",
                icon = Icons.Outlined.Assignment,
                content = {
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        placeholder = { Text("Ej: Vacuna anual", color = TextSecondary, fontSize = 15.sp) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = outlinedTextFieldColors(),
                        singleLine = true
                    )
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Mascota
            ReminderFormField(
                label = "Mascota",
                icon = Icons.Outlined.Pets,
                content = {
                    ReminderDropdownField(
                        value = selectedPet,
                        placeholder = "Seleccionar mascota",
                        options = listOf("Milo", "Luna", "Coco"),
                        onOptionSelected = { selectedPet = it }
                    )
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Tipo
            ReminderFormField(
                label = "Tipo de recordatorio",
                icon = Icons.Outlined.CalendarMonth,
                content = {
                    ReminderDropdownField(
                        value = selectedType,
                        placeholder = "Seleccionar tipo",
                        options = listOf("Vacuna", "Desparasitación", "Consulta veterinaria", "Registro de peso", "Medicamento", "Otro"),
                        onOptionSelected = { selectedType = it }
                    )
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Fecha (Usando componente reutilizable)
            ReminderFormField(
                label = "Fecha",
                icon = Icons.Outlined.CalendarMonth,
                content = {
                    PetCareDatePickerField(
                        value = date,
                        onValueChange = { date = it },
                        placeholder = "Seleccionar fecha"
                    )
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Hora (Usando componente reutilizable)
            ReminderFormField(
                label = "Hora",
                icon = Icons.Outlined.AccessTime,
                content = {
                    PetCareTimePickerField(
                        value = time,
                        onValueChange = { time = it },
                        placeholder = "Seleccionar hora"
                    )
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Repetición
            ReminderFormField(
                label = "Repetición",
                icon = Icons.Outlined.Repeat,
                content = {
                    ReminderDropdownField(
                        value = selectedRepeat,
                        placeholder = "No se repite",
                        options = listOf("No se repite", "Diario", "Semanal", "Mensual", "Anual"),
                        onOptionSelected = { selectedRepeat = it }
                    )
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Observaciones
            ReminderFormField(
                label = "Observaciones (opcional)",
                icon = Icons.Outlined.Assignment,
                content = {
                    OutlinedTextField(
                        value = observations,
                        onValueChange = { observations = it },
                        placeholder = { Text("Agrega notas adicionales...", color = TextSecondary, fontSize = 15.sp) },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3,
                        shape = RoundedCornerShape(12.dp),
                        colors = outlinedTextFieldColors()
                    )
                }
            )

            Spacer(modifier = Modifier.height(40.dp))

            PetCareButton(
                text = if (isEdit) "Guardar cambios" else "Guardar recordatorio",
                onClick = { },
                containerColor = PrimaryPurple
            )
        }
    }
}

@Composable
private fun ReminderFormField(
    label: String,
    icon: ImageVector,
    content: @Composable () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .padding(top = 4.dp)
                .size(44.dp)
                .background(PrimaryPurple.copy(alpha = 0.1f), RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = PrimaryPurple,
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            content()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ReminderDropdownField(
    value: String,
    placeholder: String,
    options: List<String>,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = {},
            readOnly = true,
            placeholder = { Text(placeholder, color = TextSecondary, fontSize = 15.sp) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                .fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = outlinedTextFieldColors()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { selectionOption ->
                DropdownMenuItem(
                    text = { Text(selectionOption) },
                    onClick = {
                        onOptionSelected(selectionOption)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun outlinedTextFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = PrimaryPurple,
    unfocusedBorderColor = Border,
    focusedContainerColor = Color.White,
    unfocusedContainerColor = Color.White,
    focusedTextColor = TextPrimary,
    unfocusedTextColor = TextPrimary
)