package com.petcare.app.screens.weight

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.petcare.app.components.PetCareButton
import com.petcare.app.components.PetCareDatePickerField
import com.petcare.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddWeightScreen(
    onBackClick: () -> Unit = {}
) {
    var selectedPet by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var observations by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Registrar peso",
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
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Background
                )
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
                text = "Registra el peso de tu mascota para llevar un seguimiento de su salud.",
                fontSize = 15.sp,
                color = TextSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // Campo: Mascota
            WeightFormField(
                label = "Mascota",
                icon = Icons.Outlined.Pets,
                content = {
                    WeightDropdownField(
                        value = selectedPet,
                        placeholder = "Seleccionar mascota",
                        options = listOf("Milo", "Luna", "Coco"),
                        onOptionSelected = { selectedPet = it }
                    )
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Campo: Peso Actual
            WeightFormField(
                label = "Peso actual",
                icon = Icons.Outlined.MonitorWeight,
                content = {
                    Column {
                        OutlinedTextField(
                            value = weight,
                            onValueChange = { weight = it },
                            placeholder = { Text("Ej: 25.5", color = TextSecondary, fontSize = 15.sp) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = weightTextFieldColors(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            suffix = {
                                Text("kg", color = TextPrimary, fontWeight = FontWeight.Medium)
                            },
                            singleLine = true
                        )
                        Text(
                            text = "Ingresa el peso en kilogramos (kg).",
                            fontSize = 12.sp,
                            color = TextSecondary,
                            modifier = Modifier.padding(start = 4.dp, top = 4.dp)
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Campo: Fecha del registro (REEMPLAZADO POR COMPONENTE REUTILIZABLE)
            WeightFormField(
                label = "Fecha del registro",
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

            // Campo: Observaciones
            WeightFormField(
                label = "Observaciones (opcional)",
                icon = Icons.Outlined.Description,
                content = {
                    Column {
                        OutlinedTextField(
                            value = observations,
                            onValueChange = { if (it.length <= 200) observations = it },
                            placeholder = {
                                Text("Agrega notas adicionales...", color = TextSecondary, fontSize = 15.sp)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 3,
                            shape = RoundedCornerShape(12.dp),
                            colors = weightTextFieldColors()
                        )
                        Text(
                            text = "${observations.length}/200",
                            fontSize = 12.sp,
                            color = TextSecondary,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 4.dp),
                            textAlign = TextAlign.End
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(40.dp))

            PetCareButton(
                text = "Guardar peso",
                onClick = { /* Lógica de guardado */ },
                containerColor = PrimaryPurple
            )
        }
    }
}

/**
 * Componente base para mantener la estructura visual: Icono izquierda | (Título + Campo)
 */
@Composable
private fun WeightFormField(
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

/**
 * Selector desplegable consistente con el estilo del formulario
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun WeightDropdownField(
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
            colors = weightTextFieldColors()
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

/**
 * Colores personalizados para los TextField para mantener consistencia
 */
@Composable
private fun weightTextFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = PrimaryPurple,
    unfocusedBorderColor = Border,
    focusedContainerColor = Color.White,
    unfocusedContainerColor = Color.White,
    focusedTextColor = TextPrimary,
    unfocusedTextColor = TextPrimary
)