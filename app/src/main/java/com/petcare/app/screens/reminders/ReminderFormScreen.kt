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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.petcare.app.components.PetCareButton
import com.petcare.app.components.PetCareDatePickerField
import com.petcare.app.components.PetCareTimePickerField
import com.petcare.app.data.local.entity.PetEntity
import com.petcare.app.data.local.entity.ReminderEntity
import com.petcare.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReminderFormScreen(
    pets: List<PetEntity>,
    existingReminder: ReminderEntity? = null,
    onBack: () -> Unit,
    onSave: (ReminderEntity) -> Unit
) {
    val isEditing = existingReminder != null
    
    var title by remember { mutableStateOf(existingReminder?.title ?: "") }
    var selectedPet by remember { mutableStateOf(pets.find { it.id == existingReminder?.petId }) }
    var selectedType by remember { mutableStateOf(existingReminder?.reminderType ?: "") }
    var date by remember { mutableStateOf(existingReminder?.reminderDate ?: "") }
    var time by remember { mutableStateOf(existingReminder?.reminderTime ?: "") }
    var anticipation by remember { mutableIntStateOf(existingReminder?.anticipationMinutes ?: 0) }
    var observations by remember { mutableStateOf(existingReminder?.description ?: "") }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = if (isEditing) "Editar recordatorio" else "Agregar recordatorio",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = TextPrimary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Atrás", tint = TextPrimary)
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
                    ReminderPetDropdown(
                        value = selectedPet?.nombre ?: "Seleccionar mascota",
                        pets = pets,
                        onPetSelected = { selectedPet = it }
                    )
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Tipo
            ReminderFormField(
                label = "Tipo de recordatorio",
                icon = Icons.Outlined.CalendarMonth,
                content = {
                    ReminderTypeDropdown(
                        value = selectedType,
                        onOptionSelected = { selectedType = it }
                    )
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Fecha
            ReminderFormField(
                label = "Fecha",
                icon = Icons.Outlined.CalendarMonth,
                content = {
                    PetCareDatePickerField(value = date, onValueChange = { date = it }, placeholder = "Seleccionar fecha")
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Hora
            ReminderFormField(
                label = "Hora",
                icon = Icons.Outlined.AccessTime,
                content = {
                    PetCareTimePickerField(value = time, onValueChange = { time = it }, placeholder = "Seleccionar hora")
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Anticipación
            ReminderFormField(
                label = "🔔 Avisarme",
                icon = Icons.Outlined.NotificationsActive,
                content = {
                    ReminderAnticipationDropdown(
                        value = anticipation,
                        onOptionSelected = { anticipation = it }
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
                        placeholder = { Text("Notas adicionales...", color = TextSecondary, fontSize = 15.sp) },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3,
                        shape = RoundedCornerShape(12.dp),
                        colors = outlinedTextFieldColors()
                    )
                }
            )

            Spacer(modifier = Modifier.height(40.dp))

            PetCareButton(
                text = if (isEditing) "Actualizar recordatorio" else "Guardar recordatorio",
                onClick = {
                    val pet = selectedPet
                    if (title.isNotBlank() && pet != null && selectedType.isNotBlank() && date.isNotBlank()) {
                        val reminder = existingReminder?.copy(
                            petId = pet.id,
                            title = title.trim(),
                            description = observations.trim(),
                            reminderType = selectedType,
                            reminderDate = date,
                            reminderTime = time,
                            anticipationMinutes = anticipation
                        ) ?: ReminderEntity(
                            petId = pet.id,
                            title = title.trim(),
                            description = observations.trim(),
                            reminderType = selectedType,
                            reminderDate = date,
                            reminderTime = time,
                            anticipationMinutes = anticipation
                        )
                        onSave(reminder)
                    }
                },
                containerColor = PrimaryPurple,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun ReminderFormField(label: String, icon: ImageVector, content: @Composable () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
        Box(
            modifier = Modifier.padding(top = 4.dp).size(44.dp).background(PrimaryPurple.copy(alpha = 0.1f), RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = PrimaryPurple, modifier = Modifier.size(24.dp))
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = label, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextPrimary, modifier = Modifier.padding(bottom = 8.dp))
            content()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ReminderPetDropdown(value: String, pets: List<PetEntity>, onPetSelected: (PetEntity) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
        OutlinedTextField(
            value = value,
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable).fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = outlinedTextFieldColors()
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            pets.forEach { pet ->
                DropdownMenuItem(text = { Text(pet.nombre) }, onClick = { onPetSelected(pet); expanded = false })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ReminderTypeDropdown(value: String, onOptionSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val options = listOf("Vacuna", "Desparasitación", "Consulta veterinaria", "Registro de peso", "Medicamento", "Otro")
    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
        OutlinedTextField(
            value = value,
            onValueChange = {},
            readOnly = true,
            placeholder = { Text("Seleccionar tipo", color = TextSecondary) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable).fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = outlinedTextFieldColors()
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { option ->
                DropdownMenuItem(text = { Text(option) }, onClick = { onOptionSelected(option); expanded = false })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ReminderAnticipationDropdown(value: Int, onOptionSelected: (Int) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val options = listOf(
        "En el momento exacto" to 0,
        "15 minutos antes" to 15,
        "30 minutos antes" to 30,
        "1 hora antes" to 60,
        "1 día antes" to 1440
    )
    val selectedText = options.find { it.second == value }?.first ?: "En el momento exacto"

    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
        OutlinedTextField(
            value = selectedText,
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable).fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = outlinedTextFieldColors()
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { (label, minutes) ->
                DropdownMenuItem(
                    text = { Text(label) },
                    onClick = { onOptionSelected(minutes); expanded = false }
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
    unfocusedContainerColor = Color.White
)
