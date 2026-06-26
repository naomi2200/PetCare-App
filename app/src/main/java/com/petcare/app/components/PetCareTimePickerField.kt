package com.petcare.app.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.petcare.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetCareTimePickerField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "Seleccionar hora"
) {
    var showTimePicker by remember { mutableStateOf(false) }
    
    // Parseamos la hora actual si existe para que el picker abra en ese punto
    val initialHour = remember(value) { value.split(":").firstOrNull()?.toIntOrNull() ?: 9 }
    val initialMinute = remember(value) { value.split(":").lastOrNull()?.toIntOrNull() ?: 0 }
    
    val timePickerState = rememberTimePickerState(
        initialHour = initialHour,
        initialMinute = initialMinute,
        is24Hour = true
    )

    if (showTimePicker) {
        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        val formattedTime = String.format("%02d:%02d", timePickerState.hour, timePickerState.minute)
                        onValueChange(formattedTime)
                        showTimePicker = false
                    }
                ) {
                    Text("Aceptar", color = PrimaryPurple, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showTimePicker = false }) {
                    Text("Cancelar", color = TextSecondary)
                }
            },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    TimePicker(
                        state = timePickerState,
                        colors = TimePickerDefaults.colors(
                            selectorColor = PrimaryPurple,
                            periodSelectorSelectedContainerColor = PrimaryPurple.copy(alpha = 0.2f),
                            clockDialSelectedContentColor = Color.White,
                            clockDialColor = Background
                        )
                    )
                }
            }
        )
    }

    OutlinedTextField(
        value = value,
        onValueChange = { },
        readOnly = true,
        placeholder = { Text(placeholder, color = TextSecondary, fontSize = 15.sp) },
        modifier = Modifier
            .fillMaxWidth()
            .clickable { showTimePicker = true },
        enabled = false, // Bloquea teclado para forzar el uso del picker
        shape = RoundedCornerShape(12.dp),
        trailingIcon = {
            Icon(
                imageVector = Icons.Outlined.Schedule,
                contentDescription = "Reloj",
                tint = PrimaryPurple
            )
        },
        colors = OutlinedTextFieldDefaults.colors(
            disabledTextColor = TextPrimary,
            disabledBorderColor = Border,
            disabledPlaceholderColor = TextSecondary,
            disabledTrailingIconColor = PrimaryPurple,
            disabledContainerColor = Color.Transparent
        )
    )
}